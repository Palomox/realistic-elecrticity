package com.github.palomox.realistic_electricity.menus.screen;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.github.palomox.realistic_electricity.RealisticElectricity;
import com.github.palomox.realistic_electricity.capabilities.ElectricityGenerator.CurrentType;
import com.github.palomox.realistic_electricity.menus.CreativeGeneratorMenu;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CreativeGeneratorScreen extends AbstractContainerScreen<CreativeGeneratorMenu> {

	public static final String VOLTAGE_EDIT =  RealisticElectricity.prefixLangKey("screen.creative_generator.editbox.voltage");
	public static final String MAX_POWER_EDIT = RealisticElectricity.prefixLangKey("screen.creative_generator.editbox.max_power");
	public static final String SET_VALUES_BUTTON = RealisticElectricity.prefixLangKey("screen.creative_generator.button.setvalues");
	private static final int MARGIN = 8; 
	
	private final DecimalFormat formatter = new DecimalFormat("#.######") ; 
	
	private final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(RealisticElectricity.MODID,
			"textures/gui/creative_generator.png");

	private boolean firstRender = true;
	
	private EditBox voltage; 
	private EditBox maxPower;
	private CycleButton<CurrentType> currentType;
	private CycleButton<Boolean> generating;
	
	public CreativeGeneratorScreen(CreativeGeneratorMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		this.imageHeight = 116;
		this.imageWidth = 180;
		
		this.titleLabelX = 40;
		this.titleLabelY = 5;
				
		var formatSymbols = DecimalFormatSymbols.getInstance();
		
		formatSymbols.setDecimalSeparator('.');
		formatter.setDecimalFormatSymbols(formatSymbols);
	}
		
	protected void init() {
		this.leftPos = (this.width-this.imageWidth)/2;
		this.topPos = (this.height-this.imageHeight)/2; 
		
		voltage = new EditBox(this.font, this.leftPos+MARGIN, this.topPos+30, 100, 10, Component.translatable(VOLTAGE_EDIT));
		voltage.setValue("" + menu.getVoltage()); 
		voltage.setResponder(content -> {
			filter(voltage, content);
		});
		
		maxPower = new EditBox(this.font, this.leftPos+MARGIN, this.topPos+55, 100, 10, Component.translatable(MAX_POWER_EDIT));
		maxPower.setValue("" + menu.getMaxPower());
		maxPower.setResponder(content -> {
			filter(maxPower, content);
		});
		
		this.addRenderableWidget(voltage);
		this.addRenderableWidget(maxPower);
		
		this.addRenderableWidget(Button.builder(Component.translatable(SET_VALUES_BUTTON), (button) -> {
			setValues();
		})
				.bounds(this.leftPos+MARGIN+105, this.topPos+29, 60, 37)
				.build());
		
		

		
		currentType = this.addRenderableWidget(CycleButton.<CurrentType>builder(currentType -> switch (currentType) {
		case AC -> Component.literal("AC");
		case DC -> Component.literal("DC"); })
				.withValues(CurrentType.values())
				.displayOnlyValue()
				.withInitialValue(menu.getCurrentType())
				.create(this.leftPos+MARGIN-1, this.topPos+85, 80, 20, Component.empty(), (button, status) -> {
					setCurrentType(status);
				}));
		
		generating = this.addRenderableWidget(CycleButton.booleanBuilder(Component.literal("ON").withStyle(ChatFormatting.DARK_GREEN), Component.literal("OFF").withStyle(ChatFormatting.DARK_RED))
				.withInitialValue(menu.isGenerating())
				.displayOnlyValue()
				.create(this.leftPos+MARGIN+85, this.topPos+85, 80, 20, Component.empty(), (button, status) -> {
					setGenerating(status);
				}));
		this.setInitialFocus(voltage);
		
		
	}

	
	private void filter(EditBox editBox, String content) {
		String filtered = content.replaceAll("[A-Za-z\\s_ñç,]", "");
		if(filtered.equals(content)) {
			return; 
		}
		editBox.setValue(filtered);
	}
	
	private void setGenerating(boolean generating) {
		menu.setGenerating(generating);
		menu.syncToServer();
	} 
	
	private void setCurrentType(CurrentType currentType) {
		menu.setType(currentType);
		menu.syncToServer();
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		
		if(firstRender) {
			firstRender = false;
			voltage.setValue(formatter.format(menu.getVoltage()));
			maxPower.setValue(formatter.format(menu.getMaxPower()));
			currentType.setValue(menu.getCurrentType());
			generating.setValue(menu.isGenerating());
			
		}
		this.renderTooltip(guiGraphics, mouseX, mouseY);

		
	}
	
	
	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;

		guiGraphics.blit(BACKGROUND, relX, relY, 0, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		
	}
	
	@Override
	protected void containerTick() {
		super.containerTick();
	}
	
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
		guiGraphics.drawString(this.font, Component.translatable(VOLTAGE_EDIT), MARGIN, 20, 0x404040, false);
		guiGraphics.drawString(this.font, Component.translatable(MAX_POWER_EDIT), MARGIN, 45, 0x404040, false);
	}
		
	private void setValues() {
		float voltage = Float.valueOf(this.voltage.getValue());
		float maxPower = Float.valueOf(this.maxPower.getValue());
	 
		menu.setVoltage(voltage);
		menu.setMaxPower(maxPower);
		
		menu.syncToServer();
	}
}
