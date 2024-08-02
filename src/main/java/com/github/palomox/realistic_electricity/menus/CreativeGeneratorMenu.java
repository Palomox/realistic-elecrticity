package com.github.palomox.realistic_electricity.menus;

import com.github.palomox.realistic_electricity.RealisticElectricity;
import com.github.palomox.realistic_electricity.Registration;
import com.github.palomox.realistic_electricity.blocks.entities.CreativeGeneratorBE;
import com.github.palomox.realistic_electricity.capabilities.ElectricityGenerator.CurrentType;
import com.github.palomox.realistic_electricity.menus.sync.Sync;
import com.github.palomox.realistic_electricity.menus.sync.SyncableMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class CreativeGeneratorMenu extends SyncableMenu {

	private BlockPos position;

	private CreativeGeneratorBE blockEntity;
	
	@Sync
	protected boolean generating;
	@Sync
	protected CurrentType type;
	@Sync
	protected float voltage;
	protected float current;
	@Sync
	protected float maxPower;

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
		super(Registration.CREATIVE_GENERATOR_MENU.get(), containerId, playerInventory.player);
		
		this.position = pos;

		this.cla = access;
		
		if (playerEntity.level().getBlockEntity(this.position) instanceof CreativeGeneratorBE blockEntity) {
			this.blockEntity = blockEntity;
			
			this.generating = this.blockEntity.isGenerating();
			this.type = this.blockEntity.getCurrentType();
			this.voltage = this.blockEntity.getVoltage();
			this.maxPower = this.blockEntity.getMaxPower();
		} else {
			RealisticElectricity.LOGGER.error("Tried to open CreativeGenerator menu for a block that is not the creative generator.");
		}
		
		initSync();

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
	
	@Override
	public void receiveSync(CompoundTag nbt) {
		super.receiveSync(nbt);
		this.blockEntity.setVoltage(this.voltage);
		this.blockEntity.setMaxPower(this.maxPower);
		this.blockEntity.setCurrentType(this.type);
		this.blockEntity.setGenerating(this.generating);
		this.blockEntity.setChanged();
	}
	

	public void setGenerating(boolean generating) {
		this.generating = generating;
		this.blockEntity.setGenerating(this.generating);
		this.blockEntity.setChanged();

	}


	public CurrentType getCurrentType() {
		return type;
	}


	public void setType(CurrentType type) {
		this.type = type;
		this.blockEntity.setCurrentType(this.type);
		this.blockEntity.setChanged();

	}


	public float getVoltage() {
		return voltage;
	}


	public void setVoltage(float voltage) {
		this.voltage = voltage;
		this.blockEntity.setVoltage(this.voltage);
		this.blockEntity.setChanged();

	}
	
	public float getMaxPower() {
		return maxPower;
	}


	public void setMaxPower(float maxPower) {
		this.maxPower = maxPower;
		this.blockEntity.setMaxPower(this.maxPower);
		this.blockEntity.setChanged();

	}


	public float getCurrent() {
		return current;
	}	
}
