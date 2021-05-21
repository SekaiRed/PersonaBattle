package com.sekai.personabattlemod.move.declarations;

import com.sekai.personabattlemod.move.property.MoveTarget;
import com.sekai.personabattlemod.move.property.MoveType;

public class AttackBase extends MoveBase {
    private int power;
    private int accuracy;
    private int crit;

    public AttackBase(String name, MoveType type, MoveTarget target, int power, int accuracy, int crit, int cost) {
        super(name, type, target, cost);

        this.power = power;
        this.accuracy = accuracy;
        this.crit = crit;
        //this.cost = cost;
    }

    public int getPower()
    { return power; }

    public int getAccuracy()
    { return accuracy; }

    public int getCrit()
    { return crit; }

    public boolean isPhysical()
    {
        return getType() == MoveType.PHYS || getType() == MoveType.BOW;
    }
}