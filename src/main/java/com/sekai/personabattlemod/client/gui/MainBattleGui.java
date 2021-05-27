package com.sekai.personabattlemod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.util.GuiUtil;
import com.sekai.personabattlemod.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

public class MainBattleGui extends Screen {
    private DynamicTexture screenTextureAlpha;
    private DynamicTexture screenTexture;
    public static final int frameAmount = 7;
    public int frame = 0;
    public int counterAmount = 1;
    public int counter = 0;
    public int pageWidth = 1280;
    public int pageHeight = 720;
    public int introCounterAmount = 15;
    public int introCounter = 0;

    public MainBattleGui() {
        super(new TranslationTextComponent("narrator.screen.p5battle.battle"));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        //GuiUtil.drawRect(width/4, height/4, width/2, height/2, 255, 255, 255, 255);
        super.render(mouseX, mouseY, partialTicks);

        if(frame != frameAmount)
            renderPageTransition(frame);
        else
            renderDebug();
    }

    private void renderDebug() {
        GuiUtil.drawRect(0, 0, width, height, 180, 0, 0, 255);

        drawString(font, "debug lol", 16, 16, 0xFFFFFF);
    }

    private void renderPageTransition(int frame) {
        //minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/animation/page_" + frame + ".png"));
        //GuiUtil.drawTexturedModalRectWithSize(0, 0,  0, 0, width, height, 1280, 720, 0);
        //GuiUtil.drawTri(0,0, width, height, 1, 0, 0, 1);

        GL11.glPushMatrix();
        Framebuffer framebuffer = Minecraft.getInstance().getFramebuffer();
        int frameWidth = framebuffer.framebufferTextureWidth;
        int frameHeight = framebuffer.framebufferTextureHeight;
        float scaleRatio = 1f*width/frameWidth;
        GL11.glScalef(scaleRatio, scaleRatio, 1f);
        //RenderSystem.bindTexture(framebuffer.framebufferTexture);
        //RenderSystem.bindTexture(screenTexture);

        //NativeImage image = screenTexture.getTextureData();
        //imageGrayscaleEffect(image);

        //screenTexture.setTextureData();
        //screenTexture.getTextureData().setPixelRGBA(screenTexture.getTextureData().getPixelRGBA());
        screenTexture.updateDynamicTexture();
        GuiUtil.drawTexturedModalRectWithSize(0,0,0,0,frameWidth,frameHeight,frameWidth,frameHeight,0);
        screenTextureAlpha.updateDynamicTexture();
        int alpha = (int) ((introCounterAmount - introCounter) * 1f / introCounterAmount * 255);
        //todo alpha is incorrect i want this to finish before the end of the counter
        alpha = Math.max(alpha, 0);
        System.out.println(alpha);
        GuiUtil.drawTexturedModalRectWithSizeWithColor(0,0,0,0,frameWidth,frameHeight,frameWidth,frameHeight,0, 255, 255,255, alpha);
        GL11.glPopMatrix();

        if(introCounter != introCounterAmount)
            return;

        GL11.glPushMatrix();
        float drawScale = 1f/pageHeight*height;
        //GL11.glTranslatef(0, height, 0);
        GL11.glScalef(drawScale, drawScale, 1f);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/animation/page_" + frame + ".png"));
        GuiUtil.drawTexturedModalRectWithSize(0, 0,  0, 0, pageWidth, pageHeight, pageWidth, pageHeight, 0);
        //minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/phone.png"));
        //GuiUtil.drawTexturedModalRectWithSize(0, 0, 0, 0, 1024, 1024, 1024, 1024, 0);
        GL11.glPopMatrix();
    }

    private void imageGrayscaleEffect(NativeImage image) {
        for(int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int rgba = image.getPixelRGBA(i, j);
                /*int a = (rgba)&0xFF;
                int b = (rgba>>8)&0xFF;
                int g = (rgba>>16)&0xFF;
                int r = (rgba>>24)&0xFF;*/
                int r = (rgba)&0xFF;
                int g = (rgba>>8)&0xFF;
                int b = (rgba>>16)&0xFF;
                //int a = (rgba>>24)&0xFF;

                //System.out.println("Color " + a + " " + r + " " + g + " " + b);

                int c = (int) ((0.2126f * (r/255f) + 0.7152f * (g/255f) + 0.0722f * (b/255f))*255f);

                //System.out.println(c);

                int new_rgba = c + (c<<8) + (c<<16) + (0xFF<<24);

                image.setPixelRGBA(i, j, new_rgba);
            }
        }
    }

    @Override
    public void init() {
        Framebuffer framebuffer = Minecraft.getInstance().getFramebuffer();
        NativeImage nativeimage = new NativeImage(framebuffer.framebufferTextureWidth, framebuffer.framebufferTextureHeight, false);
        RenderSystem.bindTexture(framebuffer.framebufferTexture);
        nativeimage.downloadFromTexture(0, true);
        nativeimage.flip();
        NativeImage nativeImage2 = new NativeImage(framebuffer.framebufferTextureWidth, framebuffer.framebufferTextureHeight, false);
        nativeImage2.copyImageData(nativeimage);
        screenTexture = new DynamicTexture(nativeimage);
        NativeImage image = screenTexture.getTextureData();
        imageGrayscaleEffect(image);

        screenTextureAlpha = new DynamicTexture(nativeImage2);

        minecraft.getSoundHandler().play(SimpleSound.master(RegistryHandler.BATTLE_START.get(), 1.0F, 0.25F));

        super.init();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if(introCounter != introCounterAmount) {
            /*if(introCounter == introCounterAmount - 1) {
                minecraft.getSoundHandler().play(SimpleSound.master(RegistryHandler.BATTLE_START.get(), 1.0F));
                System.out.println("played");
            }*/
            introCounter++;
            return;
        }

        if(frame == frameAmount)
            return;

        if(counter == counterAmount - 1) {
            frame++;
            counter = 0;
        } else {
            counter++;
        }
    }
}
