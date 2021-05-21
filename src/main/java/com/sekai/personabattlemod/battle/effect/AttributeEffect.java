package com.sekai.personabattlemod.battle.effect;

public class AttributeEffect {
    FighterEffect effect;
    EffectOdds odd;

    public AttributeEffect(FighterEffect effect, EffectOdds odd)
    {
        this.effect = effect;
        this.odd = odd;
    }

    public FighterEffect getEffect()
    {
        return effect;
    }

    public EffectOdds getOdd()
    {
        return odd;
    }
}
