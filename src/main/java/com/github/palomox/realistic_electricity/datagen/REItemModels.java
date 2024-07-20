package com.github.palomox.realistic_electricity.datagen;

import com.github.palomox.realistic_electricity.RealisticElectricity;
import com.github.palomox.realistic_electricity.Registration;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class REItemModels extends ItemModelProvider{

	public REItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, RealisticElectricity.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		withExistingParent(Registration.CREATIVE_GENERATOR_BLOCK.getId().getPath(), modLoc("block/creative_generator"));
		withExistingParent(Registration.CABLE_HOLDER_BLOCK.getId().getPath(), modLoc("block/cable_holder"));

	}
	
	

}
