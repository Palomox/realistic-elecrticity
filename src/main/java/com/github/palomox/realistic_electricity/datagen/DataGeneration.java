package com.github.palomox.realistic_electricity.datagen;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.github.palomox.realistic_electricity.RealisticElectricity;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid=RealisticElectricity.MODID, bus = Bus.MOD)
public class DataGeneration {
	
	@SubscribeEvent
	public static void generate(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		
		// Client generators
		generator.addProvider(event.includeClient(), new REBlockStates(output, event.getExistingFileHelper()));
		generator.addProvider(event.includeClient(), new REItemModels(output, event.getExistingFileHelper()));
		generator.addProvider(event.includeClient(), new RELanguageProvider(output, "en_US"));
		
		// Server generators
		BlockTagsProvider blockTags = new REBlockTags(output, lookupProvider, event.getExistingFileHelper());
		generator.addProvider(event.includeServer(), blockTags);
		generator.addProvider(event.includeServer(), new REItemTags(output, lookupProvider, blockTags, event.getExistingFileHelper()));
		generator.addProvider(event.includeServer(), new RERecipes(output, lookupProvider));
		generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(), 
				List.of(new LootTableProvider.SubProviderEntry(RELootTables::new, LootContextParamSets.BLOCK)), lookupProvider));
		
	}
}
