package com.sekai.personabattlemod.battle.move.attribute;

import com.sekai.personabattlemod.battle.effect.AttributeEffect;

public class InflictAttribute extends MoveAttributeBase {
    AttributeEffect effect;

    public InflictAttribute(AttributeEffect effect) {
        this.effect = effect;
    }
}
