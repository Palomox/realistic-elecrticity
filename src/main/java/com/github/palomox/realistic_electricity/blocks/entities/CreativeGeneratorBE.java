package com.github.palomox.realistic_electricity.blocks.entities;

import com.github.palomox.realistic_electricity.Registration;
import com.github.palomox.realistic_electricity.capabilities.ElectricityGenerator;

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
	
	public CreativeGeneratorBE(BlockPos pos, BlockState blockState) {
		super(Registration.CREATIVE_GENERATOR_BE_TYPE.get(), pos, blockState);
	}
	
	private ElectricityGenerator createElectricityHandler() {
		return new ElectricityGenerator();
	}
	
	public ElectricityGenerator getElectricityHandler(Direction direction) {
		// So far we don't care about direction
		return electricityHandler.get();
	}
	
	public void tickServer() {				
		
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag, Provider registries) {
		super.saveAdditional(tag, registries);
		saveClientData(tag, registries);
	}
	
	private void saveClientData(CompoundTag tag, Provider registries) {
		tag.put("Electricity", electricity.serializeNBT(registries));

	}
	
	private void loadClientData(CompoundTag tag, Provider registries) {
		if(tag.contains("Electricity")) {
			electricity.deserializeNBT(registries, tag.getCompound("Electricity"));
		}
	}
	
	@Override
	protected void loadAdditional(CompoundTag tag, Provider registries) {
		super.loadAdditional(tag, registries);
		loadClientData(tag, registries);
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
