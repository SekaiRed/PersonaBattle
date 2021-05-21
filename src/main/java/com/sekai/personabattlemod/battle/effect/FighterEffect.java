package com.sekai.personabattlemod.battle.effect;

public enum FighterEffect {
    //status effects
    KO,//cannot be inflicted and cured by regular means, need resurrection item
    DOWN,//cannot be inflicted and cured by regular means, restores next turn
    BURN,
    FREEZE,
    SHOCK,
    DIZZY,
    FORGET,
    SLEEP,
    CONFUSE,
    FEAR,
    DESPAIR,
    RAGE,
    BRAINWASH,
    HUNGER,
    BLINDNESS,
    //special ailment
    SEWN,
    //support effects (lasts 3 turns)
    ATTACK_UP,
    ATTACK_DOWN,
    DEFENSE_UP,
    DEFENSE_DOWN,
    AGILITY_UP,
    AGILITY_DOWN,
    CRITICAL_UP,
    //temporary effect (only lasts until the fighter attacks)
    NEXT_PHYS_UP,
    NEXT_MAG_UP,
    NEXT_ANY_UP;
}
