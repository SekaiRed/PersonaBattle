package com.sekai.personabattlemod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.capabilities.WildCardProvider;
import com.sekai.personabattlemod.move.MoveDatabase;
import com.sekai.personabattlemod.move.property.MoveType;
import com.sekai.personabattlemod.persona.impl.WildCard;
import com.sekai.personabattlemod.util.GuiUtil;
import com.sekai.personabattlemod.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class BetaProfileGui extends Screen {
    //MainMenuScreen
    public static final int maxItems = 8;

    //Phone Variables
    //Animation
    public float introAnim = 0f;
    public float introRate = 5f;
    public float introMax = 100f;
    //Rendering Constant
    private static final int appSize = 100;
    private static final int spreadSize = 50;
    private static final int appOffsetX = 10;
    //Pointer Position Relative To Screen
    public double phoneX = 0;
    public double phoneY = 0;
    //Currently Hovered App
    public int appIndex = 0;

    GuiUtil.rectHolder[] rectW = new GuiUtil.rectHolder[maxItems];
    GuiUtil.rectHolder[] rectB = new GuiUtil.rectHolder[maxItems];

    public BetaProfileGui() {
        super(new TranslationTextComponent("narrator.screen.p5battle.profile"));
    }

    /*public class rectHolder
    {
        public float bottomLeftX = 0; public float bottomLeftY = 0;
        public float bottomRightX = 0; public float bottomRightY = 0;
        public float topRightX = 0; public float topRightY = 0;
        public float topLeftX = 0; public float topLeftY = 0;

        public rectHolder(float minWidth, float maxWidth, float minHeight, float maxHeight, float diffPercent)
        {
            float trueX = getRandomOffset(minWidth, maxWidth); float trueY = getRandomOffset(minHeight, maxHeight);
            float trueXmin = trueX * (1 - diffPercent); float trueXmax = trueX * (1 + diffPercent);
            float trueYmin = trueY * (1 - diffPercent); float trueYmax = trueY * (1 + diffPercent);
            this.bottomLeftX = getRandomOffset(trueXmin, trueXmax); this.bottomLeftY = getRandomOffset(trueYmin, trueYmax);
            this.bottomRightX = getRandomOffset(trueXmin, trueXmax); this.bottomRightY = getRandomOffset(trueYmin, trueYmax);
            this.topRightX = getRandomOffset(trueXmin, trueXmax); this.topRightY = getRandomOffset(trueYmin, trueYmax);
            this.topLeftX = getRandomOffset(trueXmin, trueXmax); this.topLeftY = getRandomOffset(trueYmin, trueYmax);
        }

        public rectHolder(float bottomLeftX, float bottomLeftY, float bottomRightX, float bottomRightY,
                          float topRightX, float topRightY, float topLeftX, float topLeftY)
        {
            this.bottomLeftX = bottomLeftX; this.bottomLeftY = bottomLeftY;
            this.bottomRightX = bottomRightX; this.bottomRightY = bottomRightY;
            this.topRightX = topRightX; this.topRightY = topRightY;
            this.topLeftX = topLeftX; this.topLeftY = topLeftY;
        }
    }*/

	/*public GuiProfile(Stats stat) {
		this.stat = stat;
	}*/

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public void init() {
        introAnim = 0f;
        if(!minecraft.getSoundHandler().isPlaying(SimpleSound.master(RegistryHandler.PHONE_OPEN.get(), 1.0F)))
            minecraft.getSoundHandler().play(SimpleSound.master(RegistryHandler.PHONE_OPEN.get(), 1.0F));

        float subOffsetMin = 5f; float subOffsetMax = 10f;
        //float subOffsetMin = 10f; float subOffsetMax = 10f;
        for(int i = 0; i < maxItems; i++)
        {
            rectW[i] = new GuiUtil.rectHolder(10f, 100f, 25f, 30f, 0.1f);
            //rectW[i] = new rectHolder(20f, 20f);
            rectB[i] = new GuiUtil.rectHolder(rectW[i].bottomLeftX - getRandomOffset(subOffsetMin, subOffsetMax), rectW[i].bottomLeftY - getRandomOffset(subOffsetMin, subOffsetMax),
                    rectW[i].bottomRightX - getRandomOffset(subOffsetMin, subOffsetMax), rectW[i].bottomRightY - getRandomOffset(subOffsetMin, subOffsetMax),
                    rectW[i].topRightX - getRandomOffset(subOffsetMin, subOffsetMax), rectW[i].topRightY - getRandomOffset(subOffsetMin, subOffsetMax),
                    rectW[i].topLeftX - getRandomOffset(subOffsetMin, subOffsetMax), rectW[i].topLeftY - getRandomOffset(subOffsetMin, subOffsetMax));
        }
    }

    public void updateAnimation() {
        if(introAnim != introMax)
        {
            if (introAnim > introMax)
                introAnim = introMax;
            else
                introAnim += introRate;
        }
    }

    public void renderPhoneScreen() {
        int selectSpread = 10;
        int selectX = 0;
        int selectY = 0;

        for(int i = 0; i < 4; i++)
        {
            if(phoneX >= spreadSize * (i + 1) + appSize * i - appOffsetX &&
                    phoneX <= spreadSize * (i + 1) + appSize * i - appOffsetX + appSize &&
                    phoneY >= spreadSize && phoneY <= spreadSize + appSize)
            {
                if(i != appIndex)
                {
                    appIndex = i;
                    minecraft.getSoundHandler().play(SimpleSound.master(RegistryHandler.PHONE_CHANGE.get(), 1.0F));
                }
            }
        }

        selectX = spreadSize * (appIndex + 1) + appSize * appIndex - selectSpread - appOffsetX;
        selectY = spreadSize - selectSpread;

        GuiUtil.drawFreeFormRect(0, 900, 650,900, 650, 0, 0, 0, 180, 0, 0, 255);
        GuiUtil.drawRect(selectX, selectY, appSize + 2 * selectSpread, appSize + 2 * selectSpread, 0, 255, 255, 255);
        //drawRect(-1000, -1000, 10000, 10000, 0, 255, 255, 255);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/app_navi.png"));
        GuiUtil.drawTexturedModalRectWithSize(spreadSize - appOffsetX, spreadSize,  0, 0, appSize, appSize, appSize, appSize, 0);
        minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/app_stat.png"));
        GuiUtil.drawTexturedModalRectWithSize(spreadSize * 2 + appSize - appOffsetX, spreadSize,  0, 0, appSize, appSize, appSize, appSize, 0);
        minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/app_velvet.png"));
        GuiUtil.drawTexturedModalRectWithSize(spreadSize * 3 + appSize * 2 - appOffsetX, spreadSize,  0, 0, appSize, appSize, appSize, appSize, 0);
        minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/app_text.png"));
        GuiUtil.drawTexturedModalRectWithSize(spreadSize * 4 + appSize * 3 - appOffsetX, spreadSize,  0, 0, appSize, appSize, appSize, appSize, 0);
        //drawSkewedRect(selectX, selectY, appSize + 2 * selectSpread, appSize + 2 * selectSpread, 0, 0, 255, 255, 255);
        //drawRect(-1000, -1000, 10000, 10000, 0, 255, 255, 255);
        //drawSkewedRect(selectX, selectY, 10000, 10000, 0, 0, 255, 255, 255);
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    @Override
    public boolean mouseClicked(double pointX, double pointY, int pointType) {
        //0 left click
        //1 right click
        //2 middle click
        if(pointType == 0)
        {
            WildCard wc = minecraft.getInstance().player.getCapability(WildCardProvider.WC_CAP, null).orElseThrow(() -> new IllegalArgumentException("This is indeed quite the bruh moment"));

            float phoneScale = 1f/1024f*height;
            double pointerX = 0;
            double pointerY = 0;

            double P1x = 334; double P1y = 214; double P2x = P1x + 645; double P2y = P1y + 79; double P3x = P1x - 109; double P3y = P1y + 893;
            P1x *= phoneScale; P1y *= phoneScale; P2x *= phoneScale; P2y *= phoneScale; P3x *= phoneScale; P3y *= phoneScale;

            pointerX = ((P3y - P1y) * pointX - (P3x - P1x) * pointY + P3x*P1y - P3y*P1x)
                    / Math.sqrt(Math.pow(P3y - P1y,2) + Math.pow(P3x - P1x,2));

            pointerY = -((P2y - P1y) * pointX - (P2x - P1x) * pointY + P2x*P1y - P2y*P1x)
                    / Math.sqrt(Math.pow(P2y - P1y,2) + Math.pow(P2x - P1x,2));

            pointerX /= phoneScale;
            pointerY /= phoneScale;

            for(int i = 0; i < 4; i++)
            {
                if(pointerX >= spreadSize * (i + 1) + appSize * i - appOffsetX &&
                        pointerX <= spreadSize * (i + 1) + appSize * i - appOffsetX + appSize &&
                        pointerY >= spreadSize && pointerY <= spreadSize + appSize)
                {
                    if(i == 1)
                    {
                        Minecraft.getInstance().displayGuiScreen(new BetaSinglePersonaGui(wc.getEquipedPersona()));
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        updateAnimation();
        updateLogic(mouseX, mouseY);

        WildCard wc = minecraft.getInstance().player.getCapability(WildCardProvider.WC_CAP, null).orElseThrow(() -> new IllegalArgumentException("This is indeed quite the bruh moment"));

        DecimalFormat f = new DecimalFormat("##.00");

        GL11.glPushMatrix();
        drawString(font, "Level : " + wc.getLevel(), width/4 + width/2, height/2, 0xFFFFFF);
        drawString(font, "mx : " + mouseX + " my : " + mouseY, width/4 + width/2, height/2 + font.FONT_HEIGHT, 0xFFFFFF);
        drawString(font, "px : " + f.format(phoneX) + " py : " + f.format(phoneY), width/4 + width/2, height/2 + font.FONT_HEIGHT * 2, 0xFFFFFF);
        //drawString(font, "Persona : " + wc.getEquipedPersona().getName(), width/4 + width/2, height/2 + font.FONT_HEIGHT, 0xFFFFFF);
        GL11.glPopMatrix();

        super.render(mouseX, mouseY, partialTicks);

        //GL11.glPushMatrix();
        //drawRect(width*3/4, height*3/4, width/4, height/4, 65534);
        GL11.glPushMatrix();
        GuiUtil.drawSkewedRect(0, 0, 128, 32, 0, 255f, 255f, 255f, 255f);
        GuiUtil.drawSkewedRect(128/2f + 16/2f, 16/2f, 128/2f - 16, 32 - 16, 1f, 189f, 0f, 0f, 255f);
        GL11.glPopMatrix();
        //GL11.glPopMatrix();

        GL11.glPushMatrix();
        float phoneScale = 1f/1024f*height;
        GL11.glTranslatef(0, height, 0);
        GL11.glScalef(phoneScale, phoneScale, 1f);
        //GL11.glTranslatef(0, -height, 0);
        GL11.glRotatef(90 - 90 * introAnim / 100f, 0f, 0f, 1f);
        GL11.glTranslatef((1 - introAnim / 100f) * 512f, -1024, 0);
        //minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/phone.png"));
        GL11.glPushMatrix();
        GL11.glTranslatef(334, 214, 0);
        GL11.glRotatef(7f, 0f, 0f, 1f);
        renderPhoneScreen();
        GL11.glPopMatrix();
        minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/phone.png"));
        GuiUtil.drawTexturedModalRectWithSize(0, 0, 0, 0, 1024, 1024, 1024, 1024, 0);
        //GuiUtil.drawFreeFormRect(334, 214 + 8, 334 + 645, 214 + 79 + 8, 334 + 645, 214 + 79, 334, 214, 100f, 100f, 100f, 255f);
        GL11.glPopMatrix();

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
        //GL11.glTranslatef(64f / sr.getScaleFactor(), sr.getScaledHeight() - bottom / sr.getScaleFactor() - 64f / sr.getScaleFactor(), 0);
        GL11.glScalef(0.5f, 0.5f, 1f);
        GL11.glTranslatef(32f*2, height*2f - bottom/2f - 32f*2f, 50f);
        GL11.glPushMatrix();
        //GL11.glScalef(sr.getScaledHeight()/1080f, sr.getScaledHeight()/1080f, 0f);
        //GL11.glScalef(1f/sr.getScaleFactor(), 1f/sr.getScaleFactor(), 0f);
        GL11.glScalef(0.5f, 0.5f, 1f);

        /*
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
        //drawFreeFormRect(0 - 40f, bottom + 32f, right + 20f, bottom + 10f, right + 8f, 0 - 20f, 0 - 18f, 0 - 16f, 255f, 255f, 255f, 255f);
        //drawFreeFormRect(0 - 32f, bottom + 20f, right + 14f, bottom + 5f, right + 3f, 0 - 6f, 0 - 9f, 0 - 10f, 0f, 0f, 0f, 255f);

        //drawInventoryBg(64, 16, 64f, height/2, 10f, 10);


        int count = wc.getSkillCount();
        for(int i = 0; i < count; i++)
        {
            if (i <= 3)
                drawSingleSlot(wc.getSkill(i), x + 0, y + spread + i * (27 + spread), 0f);
            if (i >= 4)
                drawSingleSlot(wc.getSkill(i), x + 300f, y + spread + (i - 4) * (27 + spread), 0f);
        }*/

        GL11.glPopMatrix();
        GL11.glPopMatrix();

		/*mc.renderEngine.bindTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/type_tex.png"));
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0f);
		GL11.glRotatef(5f, 0f, 0f, 1f);
		drawFreeFormRect(0,32, 128,32, 128,0, 0,0, 0f,0f,0f,255f);
		GlStateManager.color(1f, 1f, 1f, 1f);
		drawTexturedModalRect(0, 0, 162, 0, 81, 27);
		GL11.glPopMatrix();*/
    }

    public void updateLogic(int mouseX, int mouseY)
    {
        float phoneScale = 1f/1024f*height;
        //GL11.glTranslatef(334, 214, 0);
        double pointerX = 0;
        double pointerY = 0;

        double P1x = 334; double P1y = 214; double P2x = P1x + 645; double P2y = P1y + 79; double P3x = P1x - 109; double P3y = P1y + 893;
        P1x *= phoneScale; P1y *= phoneScale; P2x *= phoneScale; P2y *= phoneScale; P3x *= phoneScale; P3y *= phoneScale;

        pointerX = ((P3y - P1y) * mouseX - (P3x - P1x) * mouseY + P3x*P1y - P3y*P1x)
                / Math.sqrt(Math.pow(P3y - P1y,2) + Math.pow(P3x - P1x,2));

        pointerY = -((P2y - P1y) * mouseX - (P2x - P1x) * mouseY + P2x*P1y - P2y*P1x)
                / Math.sqrt(Math.pow(P2y - P1y,2) + Math.pow(P2x - P1x,2));

        phoneX = pointerX / phoneScale;
        phoneY = pointerY / phoneScale;
    }

    public float getRandomOffset(float min, float max)
    {
        return (float) (Math.random() * (max - min) + min);
    }

    //@SuppressWarnings("deprecation")
    public void drawSingleSlot(String skillID, float x, float y, float rot)
    {
        //soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        MoveType type = MoveDatabase.getMove(skillID).getType();
        String name = I18n.format(MoveDatabase.getMove(skillID).getUnlocalizedName() + ".name");
        String cost = Integer.toString(MoveDatabase.getMove(skillID).getCost());
		/*int power = 0;
		if(MoveDatabase.getMove(skillID) instanceof AttackBase)
		{
			power = ((AttackBase)MoveDatabase.getMove(skillID)).getPower();
		}*/

        minecraft.getTextureManager().bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/type_tex.png"));
        //RenderSystem.bindTexture(new ResourceLocation(PersonaBattle.MOD_ID, "textures/gui/type_tex.png"));
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        //GL11.glScalef(0.5f, 0.5f, 0f);
        GL11.glRotatef(rot, 0f, 0f, 1f);
        //drawFreeFormRect(0,32, 128,32, 128,0, 0,0, 0f,0f,0f,255f);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GuiUtils.drawTexturedModalRect(0, 0, type.getTexX(), type.getTexY(), 81, 27, 0);
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
    }

	/*public void drawInventoryBg(int x, int y, float width, float height, float repetHeight, int repetition)
	{
		for(int i = 0; i < repetition; i++)
		{
			GL11.glPushMatrix();
			GL11.glRotatef((float) (Math.random() * 5f - Math.random() * 5f), 0f, 0f, 1f);
			GL11.glTranslatef(x, y + height * (i/(float)repetition), 0);
			drawRect((int)width/2, (int)repetHeight/2, (int)width/2, (int)repetHeight/2, 0xFFFFFFFF);
			GL11.glPopMatrix();
		}
	}*/

    /*public static void drawFreeFormRect(float bottomLeftX, float bottomLeftY, float bottomRightX, float bottomRightY,
                                        float topRightX, float topRightY, float topLeftX, float topLeftY, float r, float g, float b, float a)
    {
        float f3 = a/255f; //alpha
        float f = r/255f; //red
        float f1 = g/255f; //green
        float f2 = b/255f; //blue

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GL11.glColor4f(f, f1, f2, f3);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        buffer.func_225582_a_(bottomLeftX,    bottomLeftY, 0).endVertex();
        buffer.func_225582_a_( bottomRightX,    bottomRightY, 0).endVertex();
        buffer.func_225582_a_( topRightX, topRightY, 0).endVertex();
        buffer.func_225582_a_(topLeftX, topLeftY, 0).endVertex();
        tessellator.draw();

        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    public static void drawRect(float x, float y, float width, float height, float r, float g, float b, float a)
    {
        //float skew = 16f;
        float f3 = a/255f; //alpha
        float f = r/255f; //red
        float f1 = g/255f; //green
        float f2 = b/255f; //blue
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.func_227740_m_();
        //GlStateManager.disableTexture2D();
        GlStateManager.func_227644_a_(GlStateManager.SourceFactor.SRC_ALPHA.field_225655_p_, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.field_225654_o_, GlStateManager.SourceFactor.ONE.field_225655_p_, GlStateManager.DestFactor.ZERO.field_225654_o_);
        GL11.glColor4f(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);

        bufferbuilder.func_225582_a_(x, y + height, 0.0D).endVertex();
        bufferbuilder.func_225582_a_(x + width, y + height, 0.0D).endVertex();
        bufferbuilder.func_225582_a_(x + width, y, 0.0D).endVertex();
        bufferbuilder.func_225582_a_(x, y, 0.0D).endVertex();
        tessellator.draw();
        //GlStateManager.enableTexture2D();
        GlStateManager.func_227737_l_();
    }

    public static void drawSkewedRect(float x, float y, float width, float height, float skew, float r, float g, float b, float a)
    {
        //float skew = 16f;
        float f3 = a/255f; //alpha
        float f = r/255f; //red
        float f1 = g/255f; //green
        float f2 = b/255f; //blue
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.func_227740_m_();
        //GlStateManager.disableTexture2D();
        GlStateManager.func_227644_a_(GlStateManager.SourceFactor.SRC_ALPHA.field_225655_p_, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.field_225654_o_, GlStateManager.SourceFactor.ONE.field_225655_p_, GlStateManager.DestFactor.ZERO.field_225654_o_);
        GL11.glColor4f(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);

        bufferbuilder.func_225582_a_(x - skew/2, y + height, 0.0D).endVertex();
        bufferbuilder.func_225582_a_(x + width - skew/2, y + height, 0.0D).endVertex();
        bufferbuilder.func_225582_a_(x + width + skew/2, y, 0.0D).endVertex();
        bufferbuilder.func_225582_a_(x + skew/2, y, 0.0D).endVertex();
        tessellator.draw();
        //GlStateManager.enableTexture2D();
        GlStateManager.func_227737_l_();
    }

    public static void drawTexturedModalRectWithSize(int x, int y, int u, int v, int width, int height, int spriteWidth, int spriteHeight, float zLevel)
    {
        final float uScale = 1f / spriteWidth;
        final float vScale = 1f / spriteHeight;

        GlStateManager.func_227740_m_();
        GlStateManager.func_227644_a_(GlStateManager.SourceFactor.SRC_ALPHA.field_225655_p_, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.field_225654_o_, GlStateManager.SourceFactor.ONE.field_225655_p_, GlStateManager.DestFactor.ZERO.field_225654_o_);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        wr.func_225582_a_(x        , y + height, zLevel).func_225583_a_( u          * uScale, ((v + height) * vScale)).endVertex();
        wr.func_225582_a_(x + width, y + height, zLevel).func_225583_a_((u + width) * uScale, ((v + height) * vScale)).endVertex();
        wr.func_225582_a_(x + width, y         , zLevel).func_225583_a_((u + width) * uScale, ( v           * vScale)).endVertex();
        wr.func_225582_a_(x        , y         , zLevel).func_225583_a_( u          * uScale, ( v           * vScale)).endVertex();
        tessellator.draw();

        GlStateManager.func_227737_l_();
    }*/
}
