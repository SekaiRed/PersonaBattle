package com.sekai.personabattlemod.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class GuiUtil {
    public static class rectHolder
    {
        public float bottomLeftX = 0; public float bottomLeftY = 0;
        public float bottomRightX = 0; public float bottomRightY = 0;
        public float topRightX = 0; public float topRightY = 0;
        public float topLeftX = 0; public float topLeftY = 0;

        /**
         * @param minWidth minimal width
         * @param maxWidth maximal width
         * @param minHeight minimum height
         * @param maxHeight maximal height
         * @param diffPercent is a value in percent where our new value will be for example -20% and +20% for 0.2
         */
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
    }

    public static float getRandomOffset(float min, float max)
    {
        return (float) (Math.random() * (max - min) + min);
    }

    public static void drawFreeFormRect(float bottomLeftX, float bottomLeftY, float bottomRightX, float bottomRightY,
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
        buffer.pos(bottomLeftX,    bottomLeftY, 0).endVertex();
        buffer.pos( bottomRightX,    bottomRightY, 0).endVertex();
        buffer.pos( topRightX, topRightY, 0).endVertex();
        buffer.pos(topLeftX, topLeftY, 0).endVertex();
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
        //GlStateManager.func_227740_m_();
        //GlStateManager.disableTexture2D();
        //GlStateManager.func_227644_a_(GlStateManager.SourceFactor.SRC_ALPHA.field_225655_p_, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.field_225654_o_, GlStateManager.SourceFactor.ONE.field_225655_p_, GlStateManager.DestFactor.ZERO.field_225654_o_);
        GL11.glColor4f(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);

        bufferbuilder.pos(x, y + height, 0.0D).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0D).endVertex();
        bufferbuilder.pos(x + width, y, 0.0D).endVertex();
        bufferbuilder.pos(x, y, 0.0D).endVertex();
        tessellator.draw();
        //GlStateManager.enableTexture2D();
        //GlStateManager.func_227737_l_();
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
        //GlStateManager.func_227740_m_();
        //GlStateManager.disableTexture2D();
        GlStateManager.disableTexture();
        //GlStateManager.func_227644_a_(GlStateManager.SourceFactor.SRC_ALPHA.field_225655_p_, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.field_225654_o_, GlStateManager.SourceFactor.ONE.field_225655_p_, GlStateManager.DestFactor.ZERO.field_225654_o_);
        GL11.glColor4f(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);

        bufferbuilder.pos(x - skew/2, y + height, 0.0D).endVertex();
        bufferbuilder.pos(x + width - skew/2, y + height, 0.0D).endVertex();
        bufferbuilder.pos(x + width + skew/2, y, 0.0D).endVertex();
        bufferbuilder.pos(x + skew/2, y, 0.0D).endVertex();
        tessellator.draw();
        //GlStateManager.enableTexture2D();
        GlStateManager.enableTexture();
        //GlStateManager.func_227737_l_();
    }

    public static void drawTexturedModalRectWithSize(int x, int y, int u, int v, int width, int height, int spriteWidth, int spriteHeight, float zLevel)
    {
        final float uScale = 1f / spriteWidth;
        final float vScale = 1f / spriteHeight;

        //GlStateManager.func_227740_m_();
        //GlStateManager.func_227644_a_(GlStateManager.SourceFactor.SRC_ALPHA.field_225655_p_, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.field_225654_o_, GlStateManager.SourceFactor.ONE.field_225655_p_, GlStateManager.DestFactor.ZERO.field_225654_o_);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        wr.pos(x        , y + height, zLevel).tex( u          * uScale, ((v + height) * vScale)).endVertex();
        wr.pos(x + width, y + height, zLevel).tex((u + width) * uScale, ((v + height) * vScale)).endVertex();
        wr.pos(x + width, y         , zLevel).tex((u + width) * uScale, ( v           * vScale)).endVertex();
        wr.pos(x        , y         , zLevel).tex( u          * uScale, ( v           * vScale)).endVertex();
        tessellator.draw();

        //GlStateManager.func_227737_l_();
    }

    public static void drawTexturedModalRectWithSizeWithColor(int x, int y, int u, int v, int width, int height, int spriteWidth, int spriteHeight, float zLevel, int r, int g, int b, int a)
    {
        final float uScale = 1f / spriteWidth;
        final float vScale = 1f / spriteHeight;

        //GlStateManager.func_227740_m_();
        //GlStateManager.func_227644_a_(GlStateManager.SourceFactor.SRC_ALPHA.field_225655_p_, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.field_225654_o_, GlStateManager.SourceFactor.ONE.field_225655_p_, GlStateManager.DestFactor.ZERO.field_225654_o_);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
        wr.pos(x        , y + height, zLevel).color(r, g, b, a).tex( u          * uScale, ((v + height) * vScale)).endVertex();
        wr.pos(x + width, y + height, zLevel).color(r, g, b, a).tex((u + width) * uScale, ((v + height) * vScale)).endVertex();
        wr.pos(x + width, y         , zLevel).color(r, g, b, a).tex((u + width) * uScale, ( v           * vScale)).endVertex();
        wr.pos(x        , y         , zLevel).color(r, g, b, a).tex( u          * uScale, ( v           * vScale)).endVertex();
        tessellator.draw();

        //GlStateManager.func_227737_l_();
    }

    public static void drawTri(float x, float y, float width, float height, float r, float g, float b, float a)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GL11.glColor4f(r, g, b, a);
        //bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);

        bufferbuilder.pos(x, y + height, 0.0D).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0D).endVertex();
        bufferbuilder.pos(x + width, y, 0.0D).endVertex();
        tessellator.draw();
    }

    public static void drawTexturedFreeformTri(int x0, int y0, int x1, int y1, int x2, int y2, int spriteWidth, int spriteHeight, int zLevel) {
        final float uScale = 1f / spriteWidth;
        final float vScale = 1f / spriteHeight;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();
        //wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        wr.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        wr.pos(x0, y0, 0.0D).tex( x0 * uScale, y0 * vScale).endVertex();
        wr.pos(x1, y1, 0.0D).tex( x1 * uScale, y1 * vScale).endVertex();
        wr.pos(x2, y2, 0.0D).tex( x2 * uScale, y2 * vScale).endVertex();
        /*wr.pos(x        , y + height, zLevel).tex( u          * uScale, ((v + height) * vScale)).endVertex();
        wr.pos(x + width, y + height, zLevel).tex((u + width) * uScale, ((v + height) * vScale)).endVertex();
        wr.pos(x + width, y         , zLevel).tex((u + width) * uScale, ( v           * vScale)).endVertex();
        wr.pos(x        , y         , zLevel).tex( u          * uScale, ( v           * vScale)).endVertex();*/
        tessellator.draw();
    }
}
