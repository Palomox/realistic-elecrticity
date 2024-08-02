package com.github.palomox.realistic_electricity;

import com.github.palomox.realistic_electricity.blocks.CableHolderBlock;
import com.github.palomox.realistic_electricity.blocks.CreativeGeneratorBlock;
import com.github.palomox.realistic_electricity.blocks.entities.CreativeGeneratorBE;
import com.github.palomox.realistic_electricity.capabilities.ElectricityGenerator;
import com.github.palomox.realistic_electricity.menus.CreativeGeneratorMenu;
import com.github.palomox.realistic_electricity.menus.screen.CreativeGeneratorScreen;
import com.github.palomox.realistic_electricity.network.MenuSyncPacket;
import com.github.palomox.realistic_electricity.theoneprobe.TOPCompat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = RealisticElectricity.MODID, bus = Bus.MOD)
public class Registration {
	/*
	 * Deferred registries
	 */
	
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RealisticElectricity.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RealisticElectricity.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, RealisticElectricity.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, RealisticElectricity.MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, RealisticElectricity.MODID);
    
    // Blocks
    public static final DeferredBlock<CreativeGeneratorBlock> CREATIVE_GENERATOR_BLOCK = BLOCKS.register("creative_generator", CreativeGeneratorBlock::new);
    public static final DeferredBlock<CableHolderBlock> CABLE_HOLDER_BLOCK = BLOCKS.register("cable_holder", CableHolderBlock::new);

    
    // Items
    public static final DeferredItem<BlockItem> CREATIVE_GENERATOR_ITEM = ITEMS.registerSimpleBlockItem(CREATIVE_GENERATOR_BLOCK);
    public static final DeferredItem<BlockItem> CABLE_HOLDER_ITEM = ITEMS.registerSimpleBlockItem(CABLE_HOLDER_BLOCK);
    
    // Block Entity types
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CreativeGeneratorBE>> CREATIVE_GENERATOR_BE_TYPE = BLOCK_ENTITY_TYPES.register("creative_generator",
    		() -> BlockEntityType.Builder.of(CreativeGeneratorBE::new, CREATIVE_GENERATOR_BLOCK.get()).build(null));
    
    // Menu types
    public static final DeferredHolder<MenuType<?>, MenuType<CreativeGeneratorMenu>> CREATIVE_GENERATOR_MENU = MENU_TYPES.register("creative_generator", () -> IMenuTypeExtension.create((windowId, inv, data) -> {
    	return new CreativeGeneratorMenu(windowId, inv, data.readBlockPos());
    }));

    // Creative tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> REALISTIC_ELECTRICITY_TAB = CREATIVE_MODE_TABS.register(RealisticElectricity.MODID, () -> CreativeModeTab.builder()
    		.title(Component.translatable(RealisticElectricity.prefixLangKey("itemGroup")))
    		.icon(() -> CABLE_HOLDER_ITEM.get().getDefaultInstance())
    		.displayItems(((parameters, output) -> {
    			output.accept(CREATIVE_GENERATOR_ITEM.get());
    			output.accept(CABLE_HOLDER_ITEM.get());
    		}))
    		.build());
    
    /**
     * Registers all the DeferredRegisters in the event bus
     * 
     * @param modEventBus the event bus where to register the registries.
     */
	public static void init(IEventBus modEventBus) {
		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
		CREATIVE_MODE_TABS.register(modEventBus);
		BLOCK_ENTITY_TYPES.register(modEventBus);
		MENU_TYPES.register(modEventBus);
		
	}
	
	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(ElectricityGenerator.ELECTRICAL_ENERGY_BLOCK,
				CREATIVE_GENERATOR_BE_TYPE.get(), 
				(blockEntity, side) -> blockEntity.getElectricityHandler(side));
	
	}
	
	@SubscribeEvent
	public static void registerPayload(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(RealisticElectricity.MODID+"_1");
		
		registrar.playBidirectional(MenuSyncPacket.TYPE, MenuSyncPacket.STREAM_CODEC, 
				MenuSyncPacket::handle);
	}
 	
	@SubscribeEvent
	public static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(CREATIVE_GENERATOR_MENU.get(), CreativeGeneratorScreen::new);
	}
	
	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		TOPCompat.register();
	}
	
	
}
