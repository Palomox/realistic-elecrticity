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



	public static enum CurrentType {
		AC,
		DC;
	}
	
	public float getMaxCurrent() {
		return this.voltage == 0 ? 0 : this.maxPower/this.voltage;
	}
	
	public boolean canGive(float intensity) {
		return intensity <= getMaxCurrent();
	}
	
	/**
	 * This method requests a specific current at the current voltage of the network. It is ideally used after a check using
	 * {@link #getMaxCurrent()}, because it takes into account the current drawing operation is going to succeed. It tries to 
	 * satisfy the request of the net, either by returning what it was asked to, or the max it can return.
	 * 
	 * @param current the current required by the network
	 * @return the current that the generator could give
	 */
	public float requestCurrent(float current) {
		float toGive = current > getMaxCurrent() ? getMaxCurrent() : current;
		this.current = toGive;
		
		return toGive;
	}

	@Override
	public @UnknownNullability CompoundTag serializeNBT(Provider provider) {
		CompoundTag nbt = new CompoundTag();
		
		nbt.putFloat("Voltage", voltage);
		nbt.putFloat("Current", current);
		nbt.putFloat("PowerMax", maxPower);
		nbt.putInt("Type", type.ordinal());
	
		return nbt;
	}
	

	@Override
	public void deserializeNBT(Provider provider, CompoundTag nbt) {
		System.out.println(nbt.getString("Type"));
		this.type = CurrentType.values()[nbt.getInt("Type")];
		this.voltage = nbt.getFloat("Voltage");
		this.current = nbt.getFloat("Current");
		this.maxPower = nbt.getFloat("PowerMax");
	}

			
			
			
}
