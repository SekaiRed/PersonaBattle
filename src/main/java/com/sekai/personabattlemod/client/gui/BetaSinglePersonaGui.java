package com.sekai.personabattlemod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.capabilities.WildCardProvider;
import com.sekai.personabattlemod.entities.ShadowEntity;
import com.sekai.personabattlemod.move.MoveDatabase;
import com.sekai.personabattlemod.move.property.MoveType;
import com.sekai.personabattlemod.persona.impl.Persona;
import com.sekai.personabattlemod.persona.impl.WildCard;
import com.sekai.personabattlemod.util.GuiUtil;
import com.sekai.personabattlemod.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.OutlineLayerBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;

public class BetaSinglePersonaGui extends Screen {
    public static final int maxItems = 8;

    GuiUtil.rectHolder[] rectW = new GuiUtil.rectHolder[maxItems];
    GuiUtil.rectHolder[] rectB = new GuiUtil.rectHolder[maxItems];

    Persona p;
    Entity personaPreview;
    Entity personaPreviewBG;
    int timer = 0;
    WildCard wc;

    DynamicTexture screenTexture;
    int screenTexWidth, screenTexHeight;

    private Button debugSwitchPersona;

    public BetaSinglePersonaGui() {
        super(new TranslationTextComponent("narrator.screen.p5battle.persona"));
    }

    public BetaSinglePersonaGui(Persona p) {
        super(new TranslationTextComponent("narrator.screen.p5battle.persona"));
        //personaPreview = RegistryHandler.SHADOW.get().create(Minecraft.getInstance().world);

        this.p = p;
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public void init() {
        Framebuffer framebuffer = Minecraft.getInstance().getFramebuffer();
        NativeImage nativeimage = new NativeImage(framebuffer.framebufferTextureWidth, framebuffer.framebufferTextureHeight, false);
        RenderSystem.bindTexture(framebuffer.framebufferTexture);
        nativeimage.downloadFromTexture(0, true);
        nativeimage.flip();
        screenTexture = new DynamicTexture(nativeimage);
        //screenTexWidth

        wc = minecraft.getInstance().player.getCapability(WildCardProvider.WC_CAP, null).orElseThrow(() -> new IllegalArgumentException("This is indeed quite the bruh moment"));

        p = wc.getEquipedPersona();

        ResourceLocation rl = new ResourceLocation(p.getModelID());
        personaPreview = ForgeRegistries.ENTITIES.getValue(rl).create(Minecraft.getInstance().world);
        personaPreviewBG = ForgeRegistries.ENTITIES.getValue(rl).create(Minecraft.getInstance().world);
        personaPreviewBG.getPersistentData().putIntArray("fullbright", new int[]{255, 0, 0, 255});

        debugSwitchPersona = addButton(new Button(0, height - 20, 100, 20, "Next Persona", button -> {
            if(wc.getPersonaIndex() + 1 >= wc.getPersonaCount())
                wc.equipPersona(0);
            else
                wc.equipPersona(wc.getPersonaIndex() + 1);
            init();
        }));

        float subOffsetMin = 5f; float subOffsetMax = 10f;
        for(int i = 0; i < maxItems; i++)
        {
            rectW[i] = new GuiUtil.rectHolder(10f, 100f, 25f, 30f, 0.1f);
            //rectW[i] = new rectHolder(20f, 20f);
            rectB[i] = new GuiUtil.rectHolder(rectW[i].bottomLeftX - GuiUtil.getRandomOffset(subOffsetMin, subOffsetMax), rectW[i].bottomLeftY - GuiUtil.getRandomOffset(subOffsetMin, subOffsetMax),
                    rectW[i].bottomRightX - GuiUtil.getRandomOffset(subOffsetMin, subOffsetMax), rectW[i].bottomRightY - GuiUtil.getRandomOffset(subOffsetMin, subOffsetMax),
                    rectW[i].topRightX - GuiUtil.getRandomOffset(subOffsetMin, subOffsetMax), rectW[i].topRightY - GuiUtil.getRandomOffset(subOffsetMin, subOffsetMax),
                    rectW[i].topLeftX - GuiUtil.getRandomOffset(subOffsetMin, subOffsetMax), rectW[i].topLeftY - GuiUtil.getRandomOffset(subOffsetMin, subOffsetMax));
        }


    }

    @Override
    public void tick() {
        timer++;
        if(timer>=180)
            timer=-180;

        if(personaPreview != null)
            personaPreview.ticksExisted++;

        if(personaPreviewBG != null)
            personaPreviewBG.ticksExisted++;

        super.tick();
    }

    /*@Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        WildCard wc = minecraft.getInstance().player.getCapability(WildCardProvider.WC_CAP, null).orElseThrow(() -> new IllegalArgumentException("This is indeed quite the bruh moment"));

        DecimalFormat f = new DecimalFormat("##.00");

        GL11.glPushMatrix();
        drawString(font, "Level : " + p.getLevel(), width/4 + width/2, height/2, 0xFFFFFF);
        //drawString(font, "mx : " + mouseX + " my : " + mouseY, width/4 + width/2, height/2 + font.FONT_HEIGHT, 0xFFFFFF);
        drawString(font, "Persona : " + p.getName(), width/4 + width/2, height/2 + font.FONT_HEIGHT, 0xFFFFFF);
        GL11.glPopMatrix();

        super.render(mouseX, mouseY, partialTicks);

        //GL11.glPushMatrix();
        //drawRect(width*3/4, height*3/4, width/4, height/4, 65534);
        GL11.glPushMatrix();
        GuiUtil.drawSkewedRect(0, 0, 128, 32, 0, 255f, 255f, 255f, 255f);
        GuiUtil.drawSkewedRect(128/2f + 16/2f, 16/2f, 128/2f - 16, 32 - 16, 1f, 189f, 0f, 0f, 255f);
        //Minecraft.getInstance().getRenderManager().renderEntityStatic(new PigEntity(EntityType.PIG, Minecraft.getInstance().world), 0.0f, false);
        //Minecraft.getInstance().getRenderManager().renderEntityStatic(new PigEntity(EntityType.PIG, Minecraft.getInstance().world), 0.0D, 0.0D, 0.0D, 0.0f, partialTicks, RenderSystem., field_230706_i_.getRenderTypeBuffers().getBufferSource(), 500);
        GL11.glPopMatrix();
        //GL11.glPopMatrix();

        //ModelPlayer model = new ModelPlayer(10f, false);
        //model.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        float spread = 24f;
        float right = 600f;
        float bottom = 4f*27f + 5f*spread;

        //float elementHeight = 4 * 27f + 5f* spread;

        //float x = 64f * 2f;
        //float y = (height - bottom - 64f) * 2f;
        float x = 0; float y = 0;

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1f);
        GL11.glTranslatef(32f*2, height*2f - bottom/2f - 32f*2f, 50f);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1f);

        for(int i = 0; i < maxItems; i++)
        {
            if (i <= 3)
                GuiUtil.drawFreeFormRect(x + 0 - rectW[i].bottomLeftX, y + spread + i * (27 + spread) + 27 + rectW[i].bottomLeftY,
                        x + 300 + rectW[i].bottomRightX, y + spread + i * (27 + spread) + 27 + rectW[i].bottomRightY,
                        x + 300 + rectW[i].topRightX, y + spread + i * (27 + spread) - rectW[i].topRightY,
                        x + 0 - rectW[i].topLeftX, y + spread + i * (27 + spread) - rectW[i].topLeftY, 255f, 255f, 255f, 255f);
            if (i >= 4)
                GuiUtil.drawFreeFormRect(x + 300 - rectW[i].bottomLeftX, y + spread + (i - 4) * (27 + spread) + 27 + rectW[i].bottomLeftY,
                        x + 600 + rectW[i].bottomRightX, y + spread + (i - 4) * (27 + spread) + 27 + rectW[i].bottomRightY,
                        x + 600 + rectW[i].topRightX, y + spread + (i - 4) * (27 + spread) - rectW[i].topRightY,
                        x + 300 - rectW[i].topLeftX, y + spread + (i - 4) * (27 + spread) - rectW[i].topLeftY, 255f, 255f, 255f, 255f);
        }
        for(int i = 0; i < maxItems; i++)
        {
            if (i <= 3)
                GuiUtil.drawFreeFormRect(x + 0 - rectB[i].bottomLeftX, y + spread + i * (27 + spread) + 27 + rectB[i].bottomLeftY,
                        x + 300 + rectB[i].bottomRightX, y + spread + i * (27 + spread) + 27 + rectB[i].bottomRightY,
                        x + 300 + rectB[i].topRightX, y + spread + i * (27 + spread) - rectB[i].topRightY,
                        x + 0 - rectB[i].topLeftX, y + spread + i * (27 + spread) - rectB[i].topLeftY, 0f, 0f, 0f, 255f);
            if (i >= 4)
                GuiUtil.drawFreeFormRect(x + 300 - rectB[i].bottomLeftX, y + spread + (i - 4) * (27 + spread) + 27 + rectB[i].bottomLeftY,
                        x + 600 + rectB[i].bottomRightX, y + spread + (i - 4) * (27 + spread) + 27 + rectB[i].bottomRightY,
                        x + 600 + rectB[i].topRightX, y + spread + (i - 4) * (27 + spread) - rectB[i].topRightY,
                        x + 300 - rectB[i].topLeftX, y + spread + (i - 4) * (27 + spread) - rectB[i].topLeftY, 0f, 0f, 0f, 255f);
        }

        int count = wc.getSkillCount();
        for(int i = 0; i < count; i++)
        {
            if (i <= 3)
                drawSingleSlot(wc.getSkill(i), x + 0, y + spread + i * (27 + spread), 0f);
            if (i >= 4)
                drawSingleSlot(wc.getSkill(i), x + 300f, y + spread + (i - 4) * (27 + spread), 0f);
        }

        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }*/

    /*public void drawSingleSlot(String skillID, float x, float y, float rot)
    {
        MoveType type = MoveDatabase.getMove(skillID).getType();
        String name = I18n.format(MoveDatabase.getMove(skillID).getUnlocalizedName());
        String cost = Integer.toString(MoveDatabase.getMove(skillID).getCost());

        minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/type_tex2.png"));
        //RenderSystem.bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/type_tex.png"));
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(0.66f, 0.66f, 0f);//added line
        GL11.glRotatef(rot, 0f, 0f, 1f);
        //drawFreeFormRect(0,32, 128,32, 128,0, 0,0, 0f,0f,0f,255f);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        //GuiUtils.drawTexturedModalRect(0, 0, type.getTexX(), type.getTexY(), 81, 27, 0);
        GuiUtils.drawTexturedModalRect(0, 0, type.getTexX(), type.getTexY(), 122, 41, 0);
        GL11.glPushMatrix();
        float scaleFactor = (27f/this.font.FONT_HEIGHT);
        GL11.glScalef(scaleFactor, scaleFactor, 0f);
        GL11.glTranslatef((81 + 8)/scaleFactor, 0, 0);
        drawString(this.font, name, 0, 0,  0xFFFFFF);
        //drawString(this.fontRenderer, cost, 80, 0,  0xFFFFFF);
        //drawRect(0, 0, this.fontRenderer.getStringWidth("Agi"), this.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
        GL11.glPopMatrix();

        //GL11.glColor
        GL11.glPopMatrix();
    }*/

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        //WildCard wc = minecraft.getInstance().player.getCapability(WildCardProvider.WC_CAP, null).orElseThrow(() -> new IllegalArgumentException("This is indeed quite the bruh moment"));

        /*GL11.glPushMatrix();
        Framebuffer framebuffer = Minecraft.getInstance().getFramebuffer();
        int frameWidth = framebuffer.framebufferTextureWidth;
        int frameHeight = framebuffer.framebufferTextureHeight;
        float scaleRatio = 1f*width/frameWidth;
        GL11.glScalef(scaleRatio, -scaleRatio, 1f);
        GL11.glTranslatef(0f, -frameHeight, 0f);
        //RenderSystem.bindTexture(framebuffer.framebufferTexture);
        RenderSystem.bindTexture(frameBufferTexture);
        GuiUtil.drawTexturedModalRectWithSizeWithColor(0,0,0,0,frameWidth,frameHeight,frameWidth,frameHeight,0, 128,128,128,255);
        //GL11.glScalef(1f, -1f, 1f);
        //GL11.glTranslatef(0f, frameHeight, 0f);
        GL11.glPopMatrix();*/

        GL11.glPushMatrix();
        Framebuffer framebuffer = Minecraft.getInstance().getFramebuffer();
        int frameWidth = framebuffer.framebufferTextureWidth;
        int frameHeight = framebuffer.framebufferTextureHeight;
        float scaleRatio = 1f*width/frameWidth;
        GL11.glScalef(scaleRatio, scaleRatio, 1f);
        //RenderSystem.bindTexture(framebuffer.framebufferTexture);
        //RenderSystem.bindTexture(screenTexture);
        screenTexture.updateDynamicTexture();
        GuiUtil.drawTexturedModalRectWithSizeWithColor(0,0,0,0,frameWidth,frameHeight,frameWidth,frameHeight,0, 128,128,128,255);
        //GL11.glScalef(1f, -1f, 1f);
        //GL11.glTranslatef(0f, frameHeight, 0f);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        RenderSystem.pushMatrix();
        //renderPersonaAlt2(width/4, height/2 + height/4  + height/8, 60, timer);
        renderPersona(personaPreviewBG, width/4 + width/2 - width/8 + width/128, height/2 + height/4  + height/8 + width/128, -160, 60, timer);
        RenderSystem.popMatrix();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        renderPersona(personaPreview, width/4 + width/2 - width/8, height/2 + height/4  + height/8, 0, 60, timer);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        drawString(font, "Level : " + p.getLevel(), width/4 + width/2, height/2, 0xFFFFFF);
        drawString(font, "Persona : " + p.getName(), width/4 + width/2, height/2 + font.FONT_HEIGHT, 0xFFFFFF);
        drawString(font, "Ratio : " + scaleRatio, width/4 + width/2, height/2 + font.FONT_HEIGHT*2, 0xFFFFFF);
        GL11.glPopMatrix();

        super.render(mouseX, mouseY, partialTicks);

        //GL11.glPushMatrix();
        //drawRect(width*3/4, height*3/4, width/4, height/4, 65534);
        GL11.glPushMatrix();
        GuiUtil.drawSkewedRect(0, 0, 128, 32, 0, 255f, 255f, 255f, 255f);
        GuiUtil.drawSkewedRect(128/2f + 16/2f, 16/2f, 128/2f - 16, 32 - 16, 1f, 189f, 0f, 0f, 255f);
        //GuiUtil.drawRect(0,0,width/8,height/8,1f,0f,0f,1f);
        //Minecraft.getInstance().getRenderManager().renderEntityStatic(new PigEntity(EntityType.PIG, Minecraft.getInstance().world), 0.0f, false);
        //Minecraft.getInstance().getRenderManager().renderEntityStatic(new PigEntity(EntityType.PIG, Minecraft.getInstance().world), 0.0D, 0.0D, 0.0D, 0.0f, partialTicks, RenderSystem., field_230706_i_.getRenderTypeBuffers().getBufferSource(), 500);
        GL11.glPopMatrix();
        //GL11.glPopMatrix();

        //ModelPlayer model = new ModelPlayer(10f, false);
        //model.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        float spread = 24f;
        float right = 600f;
        float bottom = 4f*41f + 5f*spread;

        //float elementHeight = 4 * 27f + 5f* spread;

        //float x = 64f * 2f;
        //float y = (height - bottom - 64f) * 2f;
        float x = 0; float y = 0;

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1f);
        GL11.glTranslatef(32f*2, height*2f - bottom/2f - 32f*2f, 50f);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1f);

        for(int i = 0; i < maxItems; i++)
        {
            if (i <= 3)
                GuiUtil.drawFreeFormRect(x + 0 - rectW[i].bottomLeftX, y + spread + i * (41 + spread) + 41 + rectW[i].bottomLeftY,
                        x + 300 + rectW[i].bottomRightX, y + spread + i * (41 + spread) + 41 + rectW[i].bottomRightY,
                        x + 300 + rectW[i].topRightX, y + spread + i * (41 + spread) - rectW[i].topRightY,
                        x + 0 - rectW[i].topLeftX, y + spread + i * (41 + spread) - rectW[i].topLeftY, 255f, 255f, 255f, 255f);
            if (i >= 4)
                GuiUtil.drawFreeFormRect(x + 300 - rectW[i].bottomLeftX, y + spread + (i - 4) * (41 + spread) + 41 + rectW[i].bottomLeftY,
                        x + 600 + rectW[i].bottomRightX, y + spread + (i - 4) * (41 + spread) + 41 + rectW[i].bottomRightY,
                        x + 600 + rectW[i].topRightX, y + spread + (i - 4) * (41 + spread) - rectW[i].topRightY,
                        x + 300 - rectW[i].topLeftX, y + spread + (i - 4) * (41 + spread) - rectW[i].topLeftY, 255f, 255f, 255f, 255f);
        }
        for(int i = 0; i < maxItems; i++)
        {
            if (i <= 3)
                GuiUtil.drawFreeFormRect(x + 0 - rectB[i].bottomLeftX, y + spread + i * (41 + spread) + 41 + rectB[i].bottomLeftY,
                        x + 300 + rectB[i].bottomRightX, y + spread + i * (41 + spread) + 41 + rectB[i].bottomRightY,
                        x + 300 + rectB[i].topRightX, y + spread + i * (41 + spread) - rectB[i].topRightY,
                        x + 0 - rectB[i].topLeftX, y + spread + i * (41 + spread) - rectB[i].topLeftY, 0f, 0f, 0f, 255f);
            if (i >= 4)
                GuiUtil.drawFreeFormRect(x + 300 - rectB[i].bottomLeftX, y + spread + (i - 4) * (41 + spread) + 41 + rectB[i].bottomLeftY,
                        x + 600 + rectB[i].bottomRightX, y + spread + (i - 4) * (41 + spread) + 41 + rectB[i].bottomRightY,
                        x + 600 + rectB[i].topRightX, y + spread + (i - 4) * (41 + spread) - rectB[i].topRightY,
                        x + 300 - rectB[i].topLeftX, y + spread + (i - 4) * (41 + spread) - rectB[i].topLeftY, 0f, 0f, 0f, 255f);
        }

        int count = p.getSkillCount();
        for(int i = 0; i < count; i++)
        {
            if (i <= 3)
                drawSingleSlot(p.getSkill(i), x + 0, y + spread + i * (41 + spread), 0f);
            if (i >= 4)
                drawSingleSlot(p.getSkill(i), x + 300f, y + spread + (i - 4) * (41 + spread), 0f);
        }

        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void drawSingleSlot(String skillID, float x, float y, float rot)
    {
        MoveType type = MoveDatabase.getMove(skillID).getType();
        String name = I18n.format(MoveDatabase.getMove(skillID).getUnlocalizedName());

        minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/type_tex2.png"));
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(0.8f, 0.8f, 1f);//added line
        GL11.glRotatef(rot, 0f, 0f, 1f);
        //drawFreeFormRect(0,32, 128,32, 128,0, 0,0, 0f,0f,0f,255f);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        //GuiUtils.drawTexturedModalRect(0, 0, type.getTexX(), type.getTexY(), 81, 27, 0);
        //GuiUtils.drawTexturedModalRect(0, 0, (int) (type.getTexX()*1.51f), (int) (type.getTexY()*1.51f), 122, 41, 0);
        GuiUtil.drawTexturedModalRectWithSize(0, 0, type.getTexX(), type.getTexY(), MoveType.WIDTH, MoveType.HEIGHT, 512, 512, 0);
        GL11.glPushMatrix();
        float scaleFactor = (MoveType.HEIGHT/this.font.FONT_HEIGHT);
        GL11.glScalef(scaleFactor, scaleFactor, 0f);
        GL11.glTranslatef((MoveType.WIDTH + 8)/scaleFactor, (MoveType.HEIGHT - font.FONT_HEIGHT)/4f/scaleFactor, 0);
        drawString(this.font, name, 0, 0,  0xFFFFFF);
        //drawString(this.fontRenderer, cost, 80, 0,  0xFFFFFF);
        //drawRect(0, 0, this.fontRenderer.getStringWidth("Agi"), this.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
        GL11.glPopMatrix();

        //GL11.glColor
        GL11.glPopMatrix();
    }

    public void renderPersona(Entity entity,int posX, int posY, int depth, int scale, float rotation) {
        if(entity == null)
            return;
        //persona
        //ShadowEntity shadow = new ShadowEntity(RegistryHandler.SHADOW.get(), null);
        //shadow.prevRotationYaw=timer;
        //shadow.rotationYaw=timer;
        //shadow.prevRotationYawHead = timer;
        //shadow.rotationYawHead = timer;
        //LivingEntity p_228187_5_ = (new ShadowEntityRender(Minecraft.getInstance().getRenderManager()));
        //LivingEntity p_228187_5_ = this.minecraft.player;
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)posX, (float)posY, 1050.0F + depth);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        matrixstack.rotate(quaternion);
        matrixstack.rotate(Vector3f.YP.rotationDegrees(-rotation));
        /*float f2 = 0;//p_228187_5_.renderYawOffset;
        float f3 = 0;//p_228187_5_.rotationYaw;
        float f4 = 0;//p_228187_5_.rotationPitch;
        float f5 = 0;//p_228187_5_.prevRotationYawHead;
        float f6 = 0;//p_228187_5_.rotationYawHead;*/
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        entityrenderermanager.renderEntityStatic(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 0xF000F0);
        //renderEntityStatic(entityrenderermanager.getRenderer(p_228187_5_), 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        /*p_228187_5_.renderYawOffset = f2;
        p_228187_5_.rotationYaw = f3;
        p_228187_5_.rotationPitch = f4;
        p_228187_5_.prevRotationYawHead = f5;
        p_228187_5_.rotationYawHead = f6;*/
        RenderSystem.popMatrix();
    }

    public void renderPersonaAlt(int posX, int posY, int scale, float rotation) {
        if(personaPreview == null)
            return;

        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)posX, (float)posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);

        GuiUtil.drawRect(0,0,width,height,1f,0f,0f,1f);

        GL11.glEnable(GL11.GL_STENCIL_TEST);
        //GL11.glStencilOp( GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE );
        //GL11.glStencilFunc( GL11.GL_ALWAYS, 1, 0x00 );
        GL11.glColorMask(false, false, false, true);
        GL11.glStencilMask(0xFF);

        GuiUtil.drawRect(width/4,height/4,width/2,height/2,255f,255f,255f,255f);

        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        matrixstack.rotate(quaternion);
        matrixstack.rotate(Vector3f.YP.rotationDegrees(-rotation));
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        entityrenderermanager.renderEntityStatic(personaPreview, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 0xF000F0);
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);

        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
        //GL11.glStencilOp( GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP );
        //GL11.glStencilFunc( GL11.GL_EQUAL, 1, 0xFF );
        GuiUtil.drawRect(0,0,width,height,255f,0f,0f,255f);

        GL11.glColorMask(true, true, true, true);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);

        RenderSystem.popMatrix();
        //GL11.glDisable(GL11.GL_STENCIL_TEST);
        //GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
    }

    public void renderPersonaAlt2(int posX, int posY, int scale, float rotation) {
        if(personaPreviewBG == null)
            return;

        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)posX, (float)posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);

        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        matrixstack.rotate(quaternion);
        matrixstack.rotate(Vector3f.YP.rotationDegrees(-rotation));
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        entityrenderermanager.setRenderShadow(false);

        OutlineLayerBuffer outlinelayerbuffer = Minecraft.getInstance().getRenderTypeBuffers().getOutlineBufferSource();
        outlinelayerbuffer.setColor(255, 0, 0, 255);
        entityrenderermanager.renderEntityStatic(personaPreviewBG, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, outlinelayerbuffer, 0xF000F0);
        outlinelayerbuffer.finish();
        entityrenderermanager.setRenderShadow(true);

        RenderSystem.popMatrix();
        //GL11.glDisable(GL11.GL_STENCIL_TEST);
        //GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
    }

    /*public <E extends Entity> void renderEntityStatic(EntityRenderer<? super E> entityrenderer, double xIn, double yIn, double zIn, float rotationYawIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        //EntityRenderer<? super E> entityrenderer = this.getRenderer(entityIn);

        try {
            Vec3d vec3d = entityrenderer.getRenderOffset(entityIn, partialTicks);
            double d2 = xIn + vec3d.getX();
            double d3 = yIn + vec3d.getY();
            double d0 = zIn + vec3d.getZ();
            matrixStackIn.push();
            matrixStackIn.translate(d2, d3, d0);
            entityrenderer.render(entityIn, rotationYawIn, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            if (entityIn.canRenderOnFire()) {
                this.renderFire(matrixStackIn, bufferIn, entityIn);
            }

            matrixStackIn.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
            if (this.options.entityShadows && this.renderShadow && entityrenderer.shadowSize > 0.0F && !entityIn.isInvisible()) {
                double d1 = this.getDistanceToCamera(entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ());
                float f = (float)((1.0D - d1 / 256.0D) * (double)entityrenderer.shadowOpaque);
                if (f > 0.0F) {
                    renderShadow(matrixStackIn, bufferIn, entityIn, f, partialTicks, this.world, entityrenderer.shadowSize);
                }
            }

            if (this.debugBoundingBox && !entityIn.isInvisible() && !Minecraft.getInstance().isReducedDebug()) {
                this.renderDebugBoundingBox(matrixStackIn, bufferIn.getBuffer(RenderType.getLines()), entityIn, partialTicks);
            }

            matrixStackIn.pop();
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
            entityIn.fillCrashReport(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
            crashreportcategory1.addDetail("Assigned renderer", entityrenderer);
            crashreportcategory1.addDetail("Location", CrashReportCategory.getCoordinateInfo(xIn, yIn, zIn));
            crashreportcategory1.addDetail("Rotation", rotationYawIn);
            crashreportcategory1.addDetail("Delta", partialTicks);
            throw new ReportedException(crashreport);
        }
    }*/
}
