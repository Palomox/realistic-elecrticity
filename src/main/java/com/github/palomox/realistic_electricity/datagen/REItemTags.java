package com.github.palomox.realistic_electricity.datagen;

import java.util.concurrent.CompletableFuture;

import com.github.palomox.realistic_electricity.RealisticElectricity;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class REItemTags extends ItemTagsProvider{
	
    public REItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, blockTags.contentsGetter(), RealisticElectricity.MODID, helper);
    }

	@Override
	protected void addTags(Provider provider) {
		
	}

}
