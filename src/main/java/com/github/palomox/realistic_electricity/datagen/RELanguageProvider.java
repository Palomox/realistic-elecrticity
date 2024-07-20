package com.github.palomox.realistic_electricity.datagen;

import com.github.palomox.realistic_electricity.RealisticElectricity;
import com.github.palomox.realistic_electricity.Registration;
import com.github.palomox.realistic_electricity.blocks.CreativeGeneratorBlock;
import com.github.palomox.realistic_electricity.menus.screen.CreativeGeneratorScreen;
import com.github.palomox.realistic_electricity.theoneprobe.TOPCompat;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class RELanguageProvider extends LanguageProvider{

	public RELanguageProvider(PackOutput output, String locale) {
		super(output, RealisticElectricity.MODID, locale);
	}

	@Override
	protected void addTranslations() {
		add(Registration.CREATIVE_GENERATOR_BLOCK.get(), "Creative Generator");
		add(RealisticElectricity.MODID+".itemGroup", "Realistic Electricity");
		add(Registration.CABLE_HOLDER_BLOCK.get(), "Cable holder");
		add(CreativeGeneratorBlock.TITLE_LABEL_KEY, "Creative Generator");
		add(TOPCompat.MAX_POWER, "Max power");
		add(CreativeGeneratorScreen.CURRENT_EDIT, "Current:");
		add(CreativeGeneratorScreen.VOLTAGE_EDIT, "Voltage:");
		add(CreativeGeneratorScreen.SET_VALUES_BUTTON, "Set Values");
	}
}
