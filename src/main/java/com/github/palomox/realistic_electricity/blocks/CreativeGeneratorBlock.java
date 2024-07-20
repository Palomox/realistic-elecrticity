package com.github.palomox.realistic_electricity.blocks;

import com.github.palomox.realistic_electricity.RealisticElectricity;
import com.github.palomox.realistic_electricity.blocks.entities.CreativeGeneratorBE;
import com.github.palomox.realistic_electricity.menus.CreativeGeneratorMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CreativeGeneratorBlock extends Block implements EntityBlock {
	
	public static final String TITLE_LABEL_KEY = RealisticElectricity.MODID+".screens.creative_generator.title";

	public CreativeGeneratorBlock() {
		super(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5f).sound(SoundType.METAL));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CreativeGeneratorBE(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> blockEntityType) {
		if (level.isClientSide) {
			return null;
		}
		return (lvl, pos, st, blockEntity) -> {
			if (blockEntity instanceof CreativeGeneratorBE be) {
				be.tickServer();
			}
		};

	}
	
	@Override
	protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		return new MenuProvider() {
			
			@Override
			public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
				ContainerLevelAccess cla = ContainerLevelAccess.create(level, pos);
				return new CreativeGeneratorMenu(containerId, playerInventory, pos, cla);
			}
			
			@Override
			public Component getDisplayName() {
				return Component.translatable(TITLE_LABEL_KEY);
			}
		};
	}
		
	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
			BlockHitResult hitResult) {
		if(!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			serverPlayer.openMenu(state.getMenuProvider(level, pos), t -> t.writeBlockPos(pos));
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	
	}
	
	

}
