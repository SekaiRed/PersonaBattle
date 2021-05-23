package com.sekai.personabattlemod.battle.move.property;

import net.minecraft.util.text.TextFormatting;

public enum MoveType {
    NULL(0,0,TextFormatting.GOLD),
    PHYS(0,0,TextFormatting.GOLD),
    BOW(cst.WIDTH,0,TextFormatting.GOLD),
    FIRE(cst.WIDTH*2,0,TextFormatting.RED),
    ICE(cst.WIDTH*3,0,TextFormatting.BLUE),
    ELEC(0, cst.HEIGHT,TextFormatting.YELLOW),
    WIND(cst.WIDTH,cst.HEIGHT,TextFormatting.GREEN),
    PSY(cst.WIDTH*2,cst.HEIGHT,TextFormatting.LIGHT_PURPLE),
    NUCLEAR(cst.WIDTH*3,cst.HEIGHT,TextFormatting.AQUA),
    BLESS(0,cst.HEIGHT*2,TextFormatting.YELLOW),
    CURSE(cst.WIDTH,cst.HEIGHT*2,TextFormatting.DARK_RED),
    ALMIGHTY(cst.WIDTH*2,cst.HEIGHT*2,TextFormatting.WHITE),
    WITHER(cst.WIDTH*3,cst.HEIGHT*2,TextFormatting.GRAY),
    END(0,cst.HEIGHT*3,TextFormatting.DARK_PURPLE),
    AILMENT(cst.WIDTH,cst.HEIGHT*3,TextFormatting.DARK_PURPLE),
    SUPPORT(cst.WIDTH*2,cst.HEIGHT*3,TextFormatting.DARK_BLUE),
    HEAL(cst.WIDTH*3,cst.HEIGHT*3,TextFormatting.GREEN),
    PASSIVE(0,cst.HEIGHT*4,TextFormatting.GOLD);

    private final int locationX;
    private final int locationY;
    private final TextFormatting color;

    public int getTexX()
    {
        return this.locationX;
    }

    public int getTexY()
    {
        return this.locationY;
    }

    public TextFormatting getTextFormatColor()
    {
        return color;
    }

    MoveType(int x, int y, TextFormatting color)
    {
        this.locationX = x;
        this.locationY = y;
        this.color = color;
    }

    private static class cst {
        private final static int WIDTH = 122;
        private final static int HEIGHT = 41;
    }

    public final static int WIDTH = 122;
    public final static int HEIGHT = 41;
}
