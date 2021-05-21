package com.sekai.personabattlemod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sekai.personabattlemod.capabilities.WildCardProvider;
import com.sekai.personabattlemod.persona.impl.WildCard;
import com.sekai.personabattlemod.persona.property.PersonaStat;
import com.sekai.personabattlemod.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class GuiOverlay extends Screen {
    public GuiOverlay() {
        super(new TranslationTextComponent("narrator.screen.p5battle.overlay"));
        this.font = Minecraft.getInstance().fontRenderer;
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            Minecraft mc = Minecraft.getInstance();

            WildCard wc = mc.player.getCapability(WildCardProvider.WC_CAP, null).orElse(null);

            if(this.font != null)
                drawString(this.font, "magic : ", 0,0, 0xFFFFFF);

            /*GL11.glPushMatrix();
            Framebuffer framebuffer = Minecraft.getInstance().getFramebuffer();
            int frameWidth = framebuffer.framebufferTextureWidth;
            int frameHeight = framebuffer.framebufferTextureHeight;
            float scaleRatio = 1f;
            GL11.glScalef(scaleRatio, scaleRatio, 1f);
            RenderSystem.bindTexture(framebuffer.framebufferTexture);
            GuiUtil.drawTexturedModalRectWithSizeWithColor(0,0,0,0,frameWidth,frameHeight,frameWidth,frameHeight,0, 128,128,128,255);
            GL11.glScalef(1f, -1f, 1f);
            GL11.glTranslatef(0f, frameHeight, 0f);
            GL11.glPopMatrix();*/

			/*mc.renderEngine.bindTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/type_tex.png"));
			GL11.glPushMatrix();
			GL11.glScalef(0.5f, 0.5f, 0f);
			GL11.glRotatef(5f, 0f, 0f, 1f);
			drawTexturedModalRect(0, 0, 162, 0, 81, 27);
			GL11.glPopMatrix();*/
        }
    }

}
