package com.github.palomox.realistic_electricity.datagen;

import java.util.stream.Collectors;

import com.github.palomox.realistic_electricity.RealisticElectricity;
import com.github.palomox.realistic_electricity.Registration;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;

public class RELootTables extends VanillaBlockLoot{

	public RELootTables(Provider provider) {
		super(provider);
	}
	
	@Override
	protected void generate() {
		dropSelf(Registration.CREATIVE_GENERATOR_BLOCK.get());
		dropSelf(Registration.CABLE_HOLDER_BLOCK.get());
	}
	
	@Override
	protected Iterable<Block> getKnownBlocks() {
		return BuiltInRegistries.BLOCK.holders()
				.filter(e -> e.key().location().getNamespace().equals(RealisticElectricity.MODID))
				.map(Holder.Reference::value)
				.collect(Collectors.toList());
	}
	
}
