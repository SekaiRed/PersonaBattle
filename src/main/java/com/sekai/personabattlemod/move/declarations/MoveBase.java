package com.sekai.personabattlemod.move.declarations;

import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.battle.effect.AttributeEffect;
import com.sekai.personabattlemod.move.attribute.InflictAttribute;
import com.sekai.personabattlemod.move.attribute.MoveAttributeBase;
import com.sekai.personabattlemod.move.property.MoveTarget;
import com.sekai.personabattlemod.move.property.MoveType;

import java.util.ArrayList;
import java.util.List;

public class MoveBase {
    private final String name;
    private final MoveType type;
    private final MoveTarget target;
    //if it uses sp cost = cost, if it's physical cost = cost/100f, if it uses a weapon then durability - cost
    private final int cost;

    private final List<MoveAttributeBase> attributes = new ArrayList<>();

    public MoveBase(String name, MoveType type, MoveTarget target, int cost)//, int power, int accuracy, int crit, int cost)
    {
        this.name = name;
        this.type = type;
        this.target = target;
        this.cost = cost;
    }

    public String getStringID()
    { return PersonaBattle.MOD_ID + ":" + name; }

    public String getUnlocalizedName()
    { return "skill." + name; }

    public String getUnlocalizedDescription()
    { return "skill." + name + ".desc"; }

    public MoveType getType()
    { return type; }

    public MoveTarget getTarget()
    { return target; }

    public int getCost()
    { return cost; }

    public MoveBase inflict(AttributeEffect effect) {
        attributes.add(new InflictAttribute(effect));
        return this;
    }
}
