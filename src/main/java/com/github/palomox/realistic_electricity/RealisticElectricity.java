package com.github.palomox.realistic_electricity;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(RealisticElectricity.MODID)
public class RealisticElectricity {
    public static final String MODID = "realistic_electricity";
    public static final Logger LOGGER = LogUtils.getLogger();
    
    

    public RealisticElectricity(IEventBus modEventBus, ModContainer modContainer) {
    	Registration.init(modEventBus);
    }
    
    public static String prefixLangKey(String key) {
    	return MODID+"."+key;
    }



}
