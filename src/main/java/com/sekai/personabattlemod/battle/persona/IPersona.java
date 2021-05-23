package com.sekai.personabattlemod.battle.persona;

import com.sekai.personabattlemod.battle.persona.property.PersonaArcana;
import com.sekai.personabattlemod.battle.persona.property.PersonaStat;

public interface IPersona {
    int getLevel();
    void setLevel(int level);

    PersonaArcana getArcana();
    void setArcana(PersonaArcana arcana);

    int getMaxHP();
    void setMaxHP(int maxHP);
    int getHP();
    void setHP(int HP);

    int getMaxSP();
    void setMaxSP(int maxSP);
    int getSP();
    void setSP(int SP);

    int getMeleeDamage();
    int getMeleeAccuracy();
    int getRangedDamage();
    int getRangedAccuracy();

    int getStat(PersonaStat stat);
    void setStat(PersonaStat stat, int value);
    void addStat(PersonaStat stat, int value);
    void clearStats();

    String getSkill(int index);
    int getSkillCount();
    boolean addSkill(String id);
    void removeSkill(int index);
    void removeSkill(String id);
}
