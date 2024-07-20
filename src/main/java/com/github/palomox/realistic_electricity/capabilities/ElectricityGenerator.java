package com.github.palomox.realistic_electricity.capabilities;


import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import com.github.palomox.realistic_electricity.RealisticElectricity;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class ElectricityGenerator implements INBTSerializable<CompoundTag> {
	public static final BlockCapability<ElectricityGenerator, @Nullable Direction> ELECTRICAL_ENERGY_BLOCK = BlockCapability.createSided(
			ResourceLocation.fromNamespaceAndPath(RealisticElectricity.MODID, "electricity_generator"), 
			ElectricityGenerator.class);
	
	private CurrentType type; 
	private float voltage;
	private float maxPower;
	private float current; 
	
	public ElectricityGenerator() {
		// defaults
		this.type = CurrentType.DC; 
		this.maxPower = 0f; 
		this.voltage = 0f; 
		this.current = 0f;  
	}
	
	public void setType(CurrentType type) {
		this.type = type;
	}

	public void setVoltage(float voltage) {
		this.voltage = voltage;
	}
	
	public void setCurrent(float current) {
		this.current = current;
	}
	
	public CurrentType getType() {
		return type;
	}
	
	public float getVoltage() {
		return voltage;
	}
	
	public float getCurrent() {
		return current;
	}
	
		
	public float getMaxPower() {
		return maxPower;
	}

	public void setMaxPower(float maxPower) {
		this.maxPower = maxPower;
	}



	public static enum CurrentType{
		AC,
		DC;
	}
	
	public float getMaxCurrent() {
		return this.maxPower/this.voltage;
	}

	@Override
	public @UnknownNullability CompoundTag serializeNBT(Provider provider) {
		CompoundTag nbt = new CompoundTag();
		
		nbt.putFloat("Voltage", voltage);
		nbt.putFloat("Current", current);
		nbt.putFloat("PowerMax", maxPower);
		nbt.putString("Type", type.name());
	
		return nbt;
	}
	

	@Override
	public void deserializeNBT(Provider provider, CompoundTag nbt) {
		this.type = CurrentType.valueOf(nbt.getString("Type"));
		this.voltage = nbt.getFloat("Voltage");
		this.current = nbt.getFloat("Current");
		this.maxPower = nbt.getFloat("PowerMax");
	}

			
			
			
}
