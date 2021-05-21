package com.sekai.personabattlemod.battle.effect;

public enum EffectOdds {
    NEVER(0f),
    LOW(0.25f),
    MEDIUM(0.5f),
    HIGH(0.75f),
    ALWAYS(1f);

    private final float odd;

    EffectOdds(float odd) {
        this.odd = odd;
    }

    public float getValue()
    {
        return odd;
    }
}
