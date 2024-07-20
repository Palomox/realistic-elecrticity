package com.github.palomox.realistic_electricity.menus.screen.widgets;

import org.jetbrains.annotations.Nullable;

import com.github.palomox.realistic_electricity.RealisticElectricity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.CreateNarration;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BigButton extends Button {
	
	public static final ResourceLocation WIDGETS_LOCATION = ResourceLocation.fromNamespaceAndPath(RealisticElectricity.MODID, "widgets/big_button");
	public static final int TEXTURE_WIDTH = 256;
	public static final int TEXTURE_HEIGHT = 256;
	
	protected BigButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress,
			CreateNarration pCreateNarration) {
		super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration);
	}
	
	protected BigButton(BigButton.Builder builder) {
		this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress, builder.createNarration);
		setTooltip(builder.tooltip);
	}
	
	public static BigButton.Builder bigBuilder(Component pMessage, Button.OnPress pOnPress) {
	      return new BigButton.Builder(pMessage, pOnPress);
	}


	
	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		Font font = Minecraft.getInstance().font;
		
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

		int i = this.isHoveredOrFocused() ? 128 : 0; 
		
		guiGraphics.blitSprite(WIDGETS_LOCATION, TEXTURE_WIDTH, TEXTURE_HEIGHT, 0, i, this.getX(), this.getY(), this.width, this.height);
		guiGraphics.blitSprite(WIDGETS_LOCATION, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH-this.width+2, TEXTURE_HEIGHT/2+i-this.height+2 , this.getX()+2, this.getY()+2, this.width-2, this.height-2);

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

		
		
//		this.blit(pPoseStack, this.getX() + 2, this.getY()+2, 258 - this.width, i+ 130-this.height,
//				this.width-2, this.height-2);
//		this.blit(pPoseStack, this.getX()+this.width-1, this.getY(), 256, i, 1, this.height);
//		this.blit(pPoseStack, this.getX(), this.getY()+this.height-1, 0, i+127, this.width, 1);
		
		int j = getFGColor();
		
		guiGraphics.drawCenteredString(font, this.getMessage(), this.getX() + this.width / 2,
				this.getY() + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
		
	}
	
	
	
	
	   @OnlyIn(Dist.CLIENT)
	   public static class Builder {
	      private final Component message;
	      private final Button.OnPress onPress;
	      @Nullable
	      private Tooltip tooltip;
	      private int x;
	      private int y;
	      private int width = 150;
	      private int height = 20;
	      private Button.CreateNarration createNarration = Button.DEFAULT_NARRATION;

	      public Builder(Component pMessage, Button.OnPress pOnPress) {
	         this.message = pMessage;
	         this.onPress = pOnPress;
	      }

	      public BigButton.Builder pos(int pX, int pY) {
	         this.x = pX;
	         this.y = pY;
	         return this;
	      }

	      public BigButton.Builder width(int pWidth) {
	         this.width = pWidth;
	         return this;
	      }

	      public BigButton.Builder size(int pWidth, int pHeight) {
	         this.width = pWidth;
	         this.height = pHeight;
	         return this;
	      }

	      public BigButton.Builder bounds(int pX, int pY, int pWidth, int pHeight) {
	         return this.pos(pX, pY).size(pWidth, pHeight);
	      }

	      public BigButton.Builder tooltip(@Nullable Tooltip pTooltip) {
	         this.tooltip = pTooltip;
	         return this;
	      }

	      public BigButton.Builder createNarration(Button.CreateNarration pCreateNarration) {
	         this.createNarration = pCreateNarration;
	         return this;
	      }

	      public BigButton build() {
	         return build(BigButton::new);
	      }

	      public BigButton build(java.util.function.Function<Builder, BigButton> builder) {
	         return builder.apply(this);
	      }
	   }

}
