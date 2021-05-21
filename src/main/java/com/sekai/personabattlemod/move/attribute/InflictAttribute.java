package com.sekai.personabattlemod.move.attribute;

import com.sekai.personabattlemod.battle.effect.AttributeEffect;
import com.sekai.personabattlemod.move.declarations.AilmentBase;
import com.sekai.personabattlemod.move.property.MoveTarget;
import com.sekai.personabattlemod.move.property.MoveType;

import java.util.ArrayList;
import java.util.List;

public class InflictAttribute extends MoveAttributeBase {
    AttributeEffect effect;

    public InflictAttribute(AttributeEffect effect) {
        this.effect = effect;
    }
}
