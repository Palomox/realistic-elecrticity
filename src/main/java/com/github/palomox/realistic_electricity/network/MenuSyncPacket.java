package com.github.palomox.realistic_electricity.network;

import com.github.palomox.realistic_electricity.RealisticElectricity;
import com.github.palomox.realistic_electricity.menus.sync.SyncableMenu;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MenuSyncPacket(CompoundTag nbt, int containerId) implements CustomPacketPayload{

	public static final CustomPacketPayload.Type<MenuSyncPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticElectricity.MODID, "menu_sync"));
	
	public static final StreamCodec<ByteBuf, MenuSyncPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.COMPOUND_TAG,
			MenuSyncPacket::nbt,
			ByteBufCodecs.INT,
			MenuSyncPacket::containerId,
			MenuSyncPacket::new);
			
	public void handle(IPayloadContext ctx) {
		if (ctx.player().containerMenu instanceof SyncableMenu syncableMenu && syncableMenu.containerId == this.containerId()) {
			ctx.enqueueWork(() -> syncableMenu.receiveSync(this.nbt()));
		} else {
			RealisticElectricity.LOGGER.error("There was an error syncing the menu " + ctx.player().containerMenu.getClass().getCanonicalName());
		}
	}
		
	
	@Override
	public Type<? extends CustomPacketPayload> type() {		
		return TYPE; 
	}
	
	

}
