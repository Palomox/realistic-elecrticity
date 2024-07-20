package com.github.palomox.realistic_electricity.menus;

import com.github.palomox.realistic_electricity.Registration;
import com.github.palomox.realistic_electricity.blocks.entities.CreativeGeneratorBE;
import com.github.palomox.realistic_electricity.capabilities.ElectricityGenerator.CurrentType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class CreativeGeneratorMenu extends AbstractContainerMenu {

	private BlockPos position;

	private CreativeGeneratorBE blockEntity;

	protected boolean generating;
	protected CurrentType type;
	protected float voltage;
	protected float current;

	private ContainerLevelAccess cla;

	/**
	 *  Client constructor
	 *  
	 * @param containerId 
	 * @param playerInventory
	 * @param data
	 */
	public CreativeGeneratorMenu(int containerId, Inventory playerInventory, BlockPos pos) {
		this(containerId, playerInventory, pos, ContainerLevelAccess.NULL);
	}
	

	/**
	 *  Server constructor
	 *  
	 * @param containerId
	 * @param playerInventory
	 * @param data
	 * @param access
	 */
	public CreativeGeneratorMenu(int containerId, Inventory playerInventory, BlockPos pos,
			ContainerLevelAccess access) {
		super(Registration.CREATIVE_GENERATOR_MENU.get(), containerId);
		
		this.position = pos;

		this.cla = access;
		
		if (playerInventory.player.level().getBlockEntity(this.position) instanceof CreativeGeneratorBE blockEntity) {
			this.blockEntity = blockEntity;
			
			this.generating = false;
			this.type = this.blockEntity.getElectricityHandler(null).getType();
			this.voltage = this.blockEntity.getElectricityHandler(null).getVoltage();
			this.current = this.blockEntity.getElectricityHandler(null).getCurrent();

	
		}

	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return null;
	}

	@Override
	public boolean stillValid(Player player) {
		return AbstractContainerMenu.stillValid(this.cla, player, Registration.CREATIVE_GENERATOR_BLOCK.get());
	}


	public boolean isGenerating() {
		return generating;
	}


	public void setGenerating(boolean generating) {
		this.generating = generating;
	}


	public CurrentType getCurrentType() {
		return type;
	}


	public void setType(CurrentType type) {
		this.type = type;
	}


	public float getVoltage() {
		return voltage;
	}


	public void setVoltage(float voltage) {
		this.voltage = voltage;
	}


	public float getCurrent() {
		return current;
	}


	public void setCurrent(float current) {
		this.current = current;
	}
	
	
	

}
