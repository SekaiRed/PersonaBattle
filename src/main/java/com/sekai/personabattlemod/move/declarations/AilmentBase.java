package com.sekai.personabattlemod.move.declarations;

import com.sekai.personabattlemod.battle.effect.AttributeEffect;
import com.sekai.personabattlemod.move.property.MoveTarget;
import com.sekai.personabattlemod.move.property.MoveType;

import java.util.ArrayList;
import java.util.List;

public class AilmentBase extends MoveBase {
    private List<AttributeEffect> effects = new ArrayList<>();

    public AilmentBase(String name, MoveTarget target, int cost) {
        super(name, MoveType.AILMENT, target, cost);
    }

    public AilmentBase addEffect(AttributeEffect effect)
    {
        effects.add(effect);
        return this;
    }

    public List<AttributeEffect> getEffects()
    {
        return effects;
    }
}

