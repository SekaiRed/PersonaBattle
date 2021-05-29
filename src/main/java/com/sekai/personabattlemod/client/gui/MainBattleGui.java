package com.sekai.personabattlemod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.battle.BattleManager;
import com.sekai.personabattlemod.packets.PacketClientInitBattle;
import com.sekai.personabattlemod.packets.PacketDebugEmergencyCloseBattle;
import com.sekai.personabattlemod.util.GuiUtil;
import com.sekai.personabattlemod.util.PacketHandler;
import com.sekai.personabattlemod.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.CallbackI;

public class MainBattleGui extends Screen {
    private DynamicTexture screenTextureAlpha;
    private DynamicTexture screenTexture;
    private DynamicTexture playerFace;
    public static final int frameAmount = 7;
    public int frame = 0;
    public int counterAmount = 1;
    public int counter = 0;
    public int pageWidth = 1280;
    public int pageHeight = 720;
    public int introCounterAmount = 14;
    public int introCounter = 0;
    BattleManager.BattleInstance battle;

    //Debug Render
    public static final int screenOffset = 32;
    public static final int listWidth = 128;
    public static final int listSep = 2;
    public static final int itemHeight = 9 * 2 + listSep * 3;

    public MainBattleGui() {
        super(new TranslationTextComponent("narrator.screen.p5battle.battle"));
    }

    public MainBattleGui(BattleManager.BattleInstance battle) {
        super(new TranslationTextComponent("narrator.screen.p5battle.battle"));
        this.battle = battle;
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

    @Override
    public void onClose() {
        //PacketHandler.NET.send(PacketDistributor.SERVER.with(() -> (ServerPlayerEntity) player.getEntity()), new PacketClientInitBattle(battle));
        PacketHandler.NET.sendToServer(new PacketDebugEmergencyCloseBattle(battle.getUniqueKey()));

        super.onClose();
    }

    private void renderDebug() {
        GuiUtil.drawRect(0, 0, width, height, 64, 0, 0, 255);

        drawString(font, "Welcome to the battle", 16, 16, 0xFFFFFF);
        GL11.glColor4f(1f, 1f, 1f, 1f);

        GuiUtil.drawRect(screenOffset, screenOffset, listWidth, height - screenOffset * 2, 32, 0, 0, 255);
        GuiUtil.drawRect(width - screenOffset - listWidth, screenOffset, listWidth, height - screenOffset * 2, 32, 0, 0, 255);

        int itemX = 0;
        int itemY = 0;
        String name = "";
        String HP = "";
        String SP = "";
        itemX = screenOffset;
        for(int i = 0; i < battle.playerSide.size(); i++) {
            itemY = screenOffset + listSep * i + itemHeight * i;
            GuiUtil.drawRect(itemX + listSep, itemY + listSep, listWidth - 2 * listSep, itemHeight, 16, 0, 0, 255);
            name = I18n.format(battle.playerSide.get(i).getDisplayName().getFormattedText());
            name += " Lv." + battle.playerSide.get(i).getStat().getLevel();
            drawString(font, name, itemX + listSep * 2, itemY + listSep * 2, 0xFFFFFF);
            HP = battle.playerSide.get(i).getStat().getHP() + "/" + battle.playerSide.get(i).getStat().getMaxHP() + " HP";
            SP = battle.playerSide.get(i).getStat().getSP() + "/" + battle.playerSide.get(i).getStat().getMaxSP() + " SP";
            drawString(font, HP, itemX + listSep * 2, itemY + listSep * 2 + font.FONT_HEIGHT, 0x00FFFF);
            drawRightAlignedString(font, SP, itemX + listWidth - listSep * 2, itemY + listSep * 2 + font.FONT_HEIGHT, 0xFF00DC);
        }
        itemX = width - screenOffset - listWidth;
        for(int i = 0; i < battle.enemySide.size(); i++) {
            itemY = screenOffset + listSep * i + itemHeight * i;
            GuiUtil.drawRect(itemX + listSep, itemY + listSep, listWidth - 2 * listSep, itemHeight, 16, 0, 0, 255);
            name = I18n.format(battle.enemySide.get(i).getDisplayName().getFormattedText());
            name += " Lv." + battle.enemySide.get(i).getStat().getLevel();
            drawString(font, name, itemX + listSep * 2, itemY + listSep * 2, 0xFFFFFF);
            HP = battle.enemySide.get(i).getStat().getHP() + "/" + battle.enemySide.get(i).getStat().getMaxHP() + " HP";
            SP = battle.enemySide.get(i).getStat().getSP() + "/" + battle.enemySide.get(i).getStat().getMaxSP() + " SP";
            drawString(font, HP, itemX + listSep * 2, itemY + listSep * 2 + font.FONT_HEIGHT, 0x00FFFF);
            drawRightAlignedString(font, SP, itemX + listWidth - listSep * 2, itemY + listSep * 2 + font.FONT_HEIGHT, 0xFF00DC);
        }

        //minecraft.getTextureManager().bindTexture(minecraft.player.getLocationSkin());
        //NativeImage playerFaceImage = new NativeImage(16, 16, false);
        //playerFaceImage.downloadFromTexture(0, true);
        RenderSystem.enableAlphaTest();

        //playerFace.updateDynamicTexture();
        //GuiUtil.drawTexturedModalRectWithSize(16, 16 + 32, 8, 8, 8, 8, 16, 16, 0);
    }

    private void renderPageTransition(int frame) {
        //minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/animation/page_" + frame + ".png"));
        //GuiUtil.drawTexturedModalRectWithSize(0, 0,  0, 0, width, height, 1280, 720, 0);
        //GuiUtil.drawTri(0,0, width, height, 1, 0, 0, 1);

        GL11.glPushMatrix();
        renderDebug();
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        Framebuffer framebuffer = Minecraft.getInstance().getFramebuffer();
        int frameWidth = framebuffer.framebufferTextureWidth;
        int frameHeight = framebuffer.framebufferTextureHeight;
        float scaleRatio = 1f*width/frameWidth;
        GL11.glScalef(scaleRatio, scaleRatio, 1f);

        drawPageFlipScreenBG(frame, frameWidth, frameHeight);
        /*if(introCounter < introCounterAmount) {
            screenTextureAlpha.updateDynamicTexture();
            int alpha = (int) ((introCounterAmount - introCounter) * 1f / introCounterAmount * 255);
            //todo alpha is incorrect i want this to finish before the end of the counter
            alpha = Math.max(alpha, 0);
            System.out.println(alpha);
            GuiUtil.drawTexturedModalRectWithSizeWithColor(0, 0, 0, 0, frameWidth, frameHeight, frameWidth, frameHeight, 0, 255, 255, 255, alpha);
        }*/
        GL11.glPopMatrix();

        //if(introCounter != introCounterAmount)
        //    return;

        GL11.glPushMatrix();
        if(introCounter == introCounterAmount) {
            float drawScale = 1f / pageHeight * height;
            //GL11.glTranslatef(0, height, 0);
            GL11.glScalef(drawScale, drawScale, 1f);
            GL11.glColor4f(1f, 1f, 1f, 1f);
            minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/animation/page_" + frame + ".png"));
            GuiUtil.drawTexturedModalRectWithSize(0, 0, 0, 0, pageWidth, pageHeight, pageWidth, pageHeight, 0);
            //minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/phone.png"));
            //GuiUtil.drawTexturedModalRectWithSize(0, 0, 0, 0, 1024, 1024, 1024, 1024, 0);
        }
        GL11.glPopMatrix();
    }

    private void drawPageFlipScreenBG(int frame, int frameWidth, int frameHeight) {
        screenTexture.updateDynamicTexture();
        switch (frame) {
            case 0:
                GuiUtil.drawTexturedModalRectWithSize(0,0,0,0,frameWidth,frameHeight,frameWidth,frameHeight,0);
                break;
            case 1:
                GuiUtil.drawTexturedFreeformTri(0,0, (int) (frameWidth*377f/1280f),0, 0,frameHeight,frameWidth,frameHeight,0);
                GuiUtil.drawTexturedFreeformTri(0,frameHeight, (int) (frameWidth*377f/1280f),0, (int) (frameWidth*1223f/1280f),(int) (frameHeight*7f/8f),frameWidth,frameHeight,0);
                GuiUtil.drawTexturedFreeformTri(0,frameHeight, (int) (frameWidth*1223f/1280f),(int) (frameHeight*7f/8f), (int) (frameWidth*1287f/1280f),frameHeight,frameWidth,frameHeight,0);
                break;
            case 2:
                GuiUtil.drawTexturedFreeformTri(0,0, (int) (frameWidth*83f/128f),frameHeight, 0,frameHeight,frameWidth,frameHeight,0);
                break;
            case 3:
                GuiUtil.drawTexturedFreeformTri(0,(int) (frameHeight*220f/720f), (int) (frameWidth*72f/128f),frameHeight, 0,frameHeight,frameWidth,frameHeight,0);
                break;
            case 4:
                GuiUtil.drawTexturedFreeformTri(0,(int) (frameHeight*360f/720f), (int) (frameWidth*40f/128f),frameHeight, 0,frameHeight,frameWidth,frameHeight,0);
                break;
            case 5:
                GuiUtil.drawTexturedFreeformTri(0,(int) (frameHeight*620f/720f), (int) (frameWidth*30f/128f),frameHeight, 0,frameHeight,frameWidth,frameHeight,0);
                break;
        }
        //GuiUtil.drawTri(0,0, width, height, 1, 0, 0, 1);
        //GuiUtil.drawTexturedModalRectWithSize(0,0,0,0,frameWidth,frameHeight,frameWidth,frameHeight,0);
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

        /*minecraft.getTextureManager().bindTexture(minecraft.player.getLocationSkin());
        NativeImage playerFaceImage = new NativeImage(16, 16, false);
        playerFaceImage.downloadFromTexture(0, true);
        playerFace = new DynamicTexture(playerFaceImage);*/

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
