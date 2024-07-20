package com.github.palomox.realistic_electricity.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import com.github.palomox.realistic_electricity.RealisticElectricity;
import com.github.palomox.realistic_electricity.Registration;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class REBlockTags extends BlockTagsProvider{

	public REBlockTags(PackOutput output, CompletableFuture<Provider> lookupProvider,
			@Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, RealisticElectricity.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider provider) {
		tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.add(Registration.CREATIVE_GENERATOR_BLOCK.get(), Registration.CABLE_HOLDER_BLOCK.get());
		tag(BlockTags.NEEDS_IRON_TOOL)
			.add(Registration.CREATIVE_GENERATOR_BLOCK.get(), Registration.CABLE_HOLDER_BLOCK.get());
		
	}
	
	
	

}
