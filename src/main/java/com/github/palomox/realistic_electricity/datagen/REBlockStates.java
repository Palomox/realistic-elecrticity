package com.github.palomox.realistic_electricity.datagen;

import com.github.palomox.realistic_electricity.RealisticElectricity;
import com.github.palomox.realistic_electricity.Registration;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile.ExistingModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class REBlockStates extends BlockStateProvider {

	public REBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, RealisticElectricity.MODID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels() {
		simpleBlock(Registration.CREATIVE_GENERATOR_BLOCK.get());
		
		ExistingModelFile cableHolderModel = models().getExistingFile(Registration.CABLE_HOLDER_BLOCK.getId());

		directionalBlock(Registration.CABLE_HOLDER_BLOCK.get(), cableHolderModel);
		
	}
	
}
