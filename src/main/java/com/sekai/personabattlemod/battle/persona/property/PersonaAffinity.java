package com.sekai.personabattlemod.battle.persona.property;

public enum PersonaAffinity {
    NEUTRAL("", false),
    STRONG("STR", true),
    WEAK("WK", false),
    BLOCK("BL", true),
    DRAIN("DR", true),
    REPEL("RPL", true);

    private final String shortName;
    private final boolean isNegative;

    PersonaAffinity(String shortName, boolean isNegative){
        this.shortName = shortName;
        this.isNegative = isNegative;
    }

    public String getShortName(){
        return shortName;
    }

    public boolean isNegative(){
        return isNegative;
    }
}
