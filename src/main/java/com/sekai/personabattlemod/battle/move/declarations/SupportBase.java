package com.sekai.personabattlemod.battle.move.declarations;

import com.sekai.personabattlemod.battle.effect.FighterEffect;
import com.sekai.personabattlemod.battle.move.property.MoveTarget;
import com.sekai.personabattlemod.battle.move.property.MoveType;

import java.util.ArrayList;
import java.util.List;

public class SupportBase extends MoveBase {
    private List<FighterEffect> effects = new ArrayList<>();

    public SupportBase(String name, MoveTarget target, int cost) {
        super(name, MoveType.SUPPORT, target, cost);
    }

    public SupportBase addEffect(FighterEffect effect) {
        this.effects.add(effect);
        return this;
    }

    public List<FighterEffect> getEffects()
    {
        return effects;
    }
}
