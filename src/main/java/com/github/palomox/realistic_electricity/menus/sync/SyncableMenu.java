package com.github.palomox.realistic_electricity.menus.sync;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

import com.github.palomox.realistic_electricity.RealisticElectricity;
import com.github.palomox.realistic_electricity.capabilities.ElectricityGenerator.CurrentType;
import com.github.palomox.realistic_electricity.network.MenuSyncPacket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class SyncableMenu extends AbstractContainerMenu {

	protected Player playerEntity;

	private HashMap<String, Field> syncedFields;

	protected SyncableMenu(MenuType<?> menuType, int containerId, Player playerEntity) {
		super(menuType, containerId);

		this.playerEntity = playerEntity;

	}

	/**
	 * Starts tracking the fields that should be synced (annotated with
	 * {@link Sync}) of this menu.
	 */
	public void initSync() {
		this.syncedFields = new HashMap<>();

		for (Field field : this.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Sync.class)) {
				this.syncedFields.put(field.getName(), field);
			}
		}
	}

	/**
	 * Syncs the status of the current server menu to the client's menu via a
	 * packet.
	 */
	@SuppressWarnings("resource")
	public void syncToClient() {
		if (!playerEntity.level().isClientSide && playerEntity instanceof ServerPlayer player) {
			PacketDistributor.sendToPlayer(player, getPacket());
		} else {
			RealisticElectricity.LOGGER.error("Tried to sync to the client from the client.");
		}
	}

	/**
	 * Syncs the status of the current client menu to the server via a packet.
	 */
	public void syncToServer() {
		PacketDistributor.sendToServer(getPacket());

	}

	@Override
	public void broadcastFullState() {
		super.broadcastFullState();
		syncToClient();
	}

	/**
	 * Generates a new instance of the {@link MenuSyncPacket} used for sync
	 * containing the appropriate data of the menu
	 * 
	 * @return the {@link MenuSyncPacket} ready to be sent.
	 */
	public MenuSyncPacket getPacket() {
		return new MenuSyncPacket(getMenuData(), containerId);
	}

	/**
	 * Stores all the data in syncable fields into an NBT {@link CompoundTag}
	 * 
	 * @return the {@link CompoundTag} in which the data to be synced is stored.
	 */
	public CompoundTag getMenuData() {
		CompoundTag nbt = new CompoundTag();

		for (var entry : syncedFields.entrySet()) {
			Field field = entry.getValue();
			String name = entry.getKey();

			Class<?> fieldType = field.getType();
			field.setAccessible(true);

			try {
				if (fieldType.isAssignableFrom(byte.class)) {
					nbt.putByte(name, field.getByte(this));
				} else if (fieldType.isAssignableFrom(short.class)) {
					nbt.putShort(name, field.getShort(this));
				} else if (fieldType.isAssignableFrom(int.class)) {
					nbt.putInt(name, field.getInt(this));
				} else if (fieldType.isAssignableFrom(float.class)) {
					nbt.putFloat(name, field.getFloat(this));
				} else if (fieldType.isAssignableFrom(long.class)) {
					nbt.putLong(name, field.getLong(this));
				} else if (fieldType.isAssignableFrom(double.class)) {
					nbt.putDouble(name, field.getDouble(this));
				} else if (fieldType.isAssignableFrom(String.class)) {
					nbt.putString(name, (String) field.get(this));
				} else if (fieldType.isAssignableFrom(boolean.class)) {
					nbt.putBoolean(name, field.getBoolean(this));
				} else if (fieldType.isAssignableFrom(UUID.class)) {
					nbt.putUUID(name, (UUID) field.get(this));
				} else if (fieldType.isAssignableFrom(CurrentType.class)) {
					nbt.putInt(name, ((CurrentType) field.get(this)).ordinal());
				} else {
					throw new RuntimeException(
							String.format("The type %s is not supported in syncing!", fieldType.getName()));
				}

			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return nbt;
	}

	/**
	 * Method called with the nbt tag received from the packet
	 * 
	 * @param nbt the {@link net.minecraft.nbt.CompoundTag} that represents the
	 *            received data.
	 */
	public void receiveSync(CompoundTag nbt) {
		for (String key : nbt.getAllKeys()) {
			Field field = this.syncedFields.get(key);
			if (field == null) {
				RealisticElectricity.LOGGER.error("Syncing mismatch for menu "+this.getClass().getCanonicalName());
				break;
			}
			// we try to fit it
			Class<?> type = field.getType();
			field.setAccessible(true);
			try {
				if (type.isAssignableFrom(byte.class)) {
					field.setByte(this, nbt.getByte(key));
				} else if (type.isAssignableFrom(short.class)) {
					field.setShort(this, nbt.getShort(key));
				} else if (type.isAssignableFrom(int.class)) {
					field.setInt(this, nbt.getInt(key));
				} else if (type.isAssignableFrom(float.class)) {
					field.setFloat(this, nbt.getFloat(key));
				} else if (type.isAssignableFrom(long.class)) {
					field.setLong(this, nbt.getLong(key));
				} else if (type.isAssignableFrom(double.class)) {
					field.setDouble(this, nbt.getDouble(key));
				} else if (type.isAssignableFrom(String.class)) {
					field.set(this, nbt.getString(key));
				} else if (type.isAssignableFrom(boolean.class)) {
					field.setBoolean(this, nbt.getBoolean(key));
				} else if (type.isAssignableFrom(UUID.class)) {
					field.set(this, nbt.getUUID(key));
				} else if (type.isAssignableFrom(CurrentType.class)) {
					field.set(this, CurrentType.values()[nbt.getInt(key)]);
				} else {
					throw new RuntimeException(
							String.format("The type %s is not supported in syncing!", type.getName()));
				}

			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}
