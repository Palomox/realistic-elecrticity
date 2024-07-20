package com.github.palomox.realistic_electricity.theoneprobe;

import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.github.palomox.realistic_electricity.RealisticElectricity;
import com.github.palomox.realistic_electricity.Registration;
import com.github.palomox.realistic_electricity.blocks.CreativeGeneratorBlock;
import com.github.palomox.realistic_electricity.blocks.entities.CreativeGeneratorBE;
import com.github.palomox.realistic_electricity.capabilities.ElectricityGenerator;

import mcjty.theoneprobe.api.Color;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;

public class TOPCompat {
	
	public static final String MAX_POWER = RealisticElectricity.prefixLangKey("labels.max_power");
	
	public static void register() {
		if (!ModList.get().isLoaded("theoneprobe")) {
			return;
		}
		InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
	}

	public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {

		public static ITheOneProbe probe;

		@Nullable
		@Override
		public Void apply(ITheOneProbe theOneProbe) {
			probe = theOneProbe;
			RealisticElectricity.LOGGER.debug("Enabled support for The One Probe");

			probe.registerProvider(new IProbeInfoProvider() {
				@Override
				public ResourceLocation getID() {
					return ResourceLocation.fromNamespaceAndPath(RealisticElectricity.MODID, "default");
				}

				@Override
				public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world,
						BlockState blockState, IProbeHitData data) {
					if (blockState.getBlock() instanceof CreativeGeneratorBlock) {

						ILayoutStyle style = probeInfo.defaultLayoutStyle().copy().borderColor(Color.rgb(255, 255, 255))
								.spacing(2);
						
						ILayoutStyle centered = probeInfo.defaultLayoutStyle().copy().alignment(ElementAlignment.ALIGN_CENTER).spacing(3);

						CreativeGeneratorBE cgenBE = world
								.getBlockEntity(data.getPos(), Registration.CREATIVE_GENERATOR_BE_TYPE.get()).get();
						
						ElectricityGenerator energy = cgenBE.getElectricityHandler(null);

						IProbeInfo row = probeInfo.horizontal(centered);
						row.vertical(style).text(Component.literal(
								String.valueOf(energy.getVoltage()) + " V (" + energy.getType().toString() + ")"));
						row.vertical(style).text(Component.literal(String.valueOf(energy.getCurrent()) + " A"));
						row.vertical().text(Component.translatable(MAX_POWER).append(": "+energy.getMaxPower()+" W"));

					}
				}
			});
			return null;
		}
	}
}
