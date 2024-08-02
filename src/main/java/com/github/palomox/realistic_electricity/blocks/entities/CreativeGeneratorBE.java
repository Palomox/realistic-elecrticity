package com.github.palomox.realistic_electricity.blocks.entities;

import com.github.palomox.realistic_electricity.Registration;
import com.github.palomox.realistic_electricity.capabilities.ElectricityGenerator;
import com.github.palomox.realistic_electricity.capabilities.ElectricityGenerator.CurrentType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;

public class CreativeGeneratorBE extends BlockEntity {
	private final ElectricityGenerator electricity = createElectricityHandler();
	private final Lazy<ElectricityGenerator> electricityHandler = Lazy.of(() -> electricity);
	
	// default values 
	private float voltage = 0.0F; 
	private boolean generating = false; 
	private float maxPower = 0.0F; 
	private CurrentType currentType = CurrentType.DC;
	
	
	public CreativeGeneratorBE(BlockPos pos, BlockState blockState) {
		super(Registration.CREATIVE_GENERATOR_BE_TYPE.get(), pos, blockState);
	}
	
	private ElectricityGenerator createElectricityHandler() {
		return new ElectricityGenerator();
	}
	
	public ElectricityGenerator getElectricityHandler(Direction direction) {
		// So far we don't care about direction. Maybe use the property to choose a phase/phase pair later on the mod dev
		return electricityHandler.get();
	}
	
	public void tickServer() {				

	}
	
	@Override
	protected void loadAdditional(CompoundTag tag, Provider registries) {
		super.loadAdditional(tag, registries);
		if(tag.contains("ElectricityCap")) {
			electricity.deserializeNBT(registries, tag.getCompound("ElectricityCap"));
		}
			this.voltage =  tag.getFloat("Voltage");
			this.maxPower = tag.getFloat("MaxPower");
		    this.generating = tag.getBoolean("Generating");
			this.currentType = CurrentType.values()[tag.getInt("CurrentType")];
		

	}

	private void loadClientData(CompoundTag tag, Provider registries) {
		loadAdditional(tag, registries);
	}
	

	
	@Override
	protected void saveAdditional(CompoundTag tag, Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("ElectricityCap", electricity.serializeNBT(registries));
		tag.putFloat("Voltage", this.voltage);
		tag.putFloat("MaxPower", this.maxPower);
		tag.putBoolean("Generating", this.generating);
		tag.putInt("CurrentType", this.currentType.ordinal());

	}
	
	private void saveClientData(CompoundTag tag, Provider registries) {
		saveAdditional(tag, registries);
	}
	
	
	
	public float getVoltage() {
		return voltage;
	}

	public void setVoltage(float voltage) {
		this.voltage = voltage;
		if (this.generating) { this.electricity.setVoltage(this.voltage); }
	}

	public boolean isGenerating() {
		return generating;
	}

	public void setGenerating(boolean generating) {
		this.generating = generating;
		if (generating) {
			// Turned machine on => put into cap
			this.electricity.setMaxPower(this.maxPower);
			this.electricity.setVoltage(this.voltage);
			this.electricity.setType(this.currentType);
		} else {
			// Turned machine off => zero out the cap
			this.electricity.setMaxPower(0);
			this.electricity.setVoltage(0);
		}
		
	}
	public float getMaxPower() {
		return maxPower;
	}

	public void setMaxPower(float maxPower) {
		this.maxPower = maxPower;
		if (generating) { this.electricity.setMaxPower(this.maxPower);}
	}

	public CurrentType getCurrentType() {
		return currentType;
	}

	public void setCurrentType(CurrentType currentType) {
		this.currentType = currentType;
		if (generating) { this.electricity.setType(this.currentType); }
	}

	
	@Override
	public CompoundTag getUpdateTag(Provider registries) {
		CompoundTag tag =  super.getUpdateTag(registries);
		saveClientData(tag, registries);
		return tag;
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag, Provider lookupProvider) {
		if (tag != null) {
			loadClientData(tag, lookupProvider);
		}
	}
	
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);			
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, Provider lookupProvider) {
		// Called in client side
		CompoundTag nbt = pkt.getTag();
		
		if (nbt != null) {
			handleUpdateTag(nbt, lookupProvider);
		}
		
	}

}
