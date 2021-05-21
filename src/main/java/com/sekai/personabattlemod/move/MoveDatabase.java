package com.sekai.personabattlemod.move;

import com.sekai.personabattlemod.battle.effect.AttributeEffect;
import com.sekai.personabattlemod.battle.effect.EffectOdds;
import com.sekai.personabattlemod.battle.effect.FighterEffect;
import com.sekai.personabattlemod.move.declarations.AilmentBase;
import com.sekai.personabattlemod.move.declarations.AttackBase;
import com.sekai.personabattlemod.move.declarations.MoveBase;
import com.sekai.personabattlemod.move.declarations.SupportBase;
import com.sekai.personabattlemod.move.property.MoveTarget;
import com.sekai.personabattlemod.move.property.MoveType;
import net.minecraft.util.text.TextFormatting;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class MoveDatabase {
    //public static final
    public static MoveBase NULL = new MoveBase("null", MoveType.NULL, MoveTarget.SELF, 0);
    public static final LinkedHashMap<String, MoveBase> moveList = new LinkedHashMap<>();
    //public static final List<Item> ITEMS = new ArrayList<Item>();

    private static int id = 0;

    public static void init()
    {
        registerMove(new AttackBase("agi", MoveType.FIRE, MoveTarget.SINGLE_OPPONENT, 40, 99, 0, 4).inflict(new AttributeEffect(FighterEffect.BURN, EffectOdds.LOW)));
        registerMove(new AttackBase("agilao", MoveType.FIRE, MoveTarget.SINGLE_OPPONENT, 100, 99, 0, 8));
        registerMove(new AttackBase("agidyne", MoveType.FIRE, MoveTarget.SINGLE_OPPONENT, 160, 99, 0, 12));
        registerMove(new AttackBase("maragi", MoveType.FIRE, MoveTarget.EVERY_OPPONENT, 40, 95, 0, 10));
        registerMove(new AttackBase("maragion", MoveType.FIRE, MoveTarget.EVERY_OPPONENT, 100, 99, 0, 16));
        registerMove(new AttackBase("maragidyne", MoveType.FIRE, MoveTarget.EVERY_OPPONENT, 160, 99, 0, 22));
        registerMove(new AttackBase("bufu", MoveType.ICE, MoveTarget.SINGLE_OPPONENT, 20, 100, 5, 6));
        registerMove(new AttackBase("zio", MoveType.ELEC, MoveTarget.SINGLE_OPPONENT, 20, 100, 5, 6));
        registerMove(new AttackBase("garu", MoveType.WIND, MoveTarget.SINGLE_OPPONENT, 40, 99, 0, 3));
        registerMove(new AttackBase("garula", MoveType.WIND, MoveTarget.SINGLE_OPPONENT, 100, 99, 0, 6));
        registerMove(new AttackBase("garudyne", MoveType.WIND, MoveTarget.SINGLE_OPPONENT, 160, 99, 0, 10));
        registerMove(new AttackBase("magaru", MoveType.WIND, MoveTarget.EVERY_OPPONENT, 40, 95, 0, 8));
        registerMove(new AttackBase("magarula", MoveType.WIND, MoveTarget.EVERY_OPPONENT, 100, 95, 0, 14));
        registerMove(new AttackBase("magarudyne", MoveType.WIND, MoveTarget.EVERY_OPPONENT, 160, 95, 0, 20));
        registerMove(new AttackBase("psi", MoveType.PSY, MoveTarget.SINGLE_OPPONENT, 20, 100, 5, 6));
        registerMove(new AttackBase("frei", MoveType.NUCLEAR, MoveTarget.SINGLE_OPPONENT, 40, 99, 0, 4));
        registerMove(new AttackBase("bufu", MoveType.ICE, MoveTarget.SINGLE_OPPONENT, 20, 100, 5, 6));
        registerMove(new AttackBase("kouha", MoveType.BLESS, MoveTarget.SINGLE_OPPONENT, 50, 99, 0, 4));
        registerMove(new AttackBase("eiha", MoveType.CURSE, MoveTarget.SINGLE_OPPONENT, 50, 99, 0, 4));
        registerMove(new AttackBase("lunge", MoveType.PHYS, MoveTarget.SINGLE_OPPONENT, 20, 90, 0, 5));
        registerMove(new AttackBase("sprayshot", MoveType.BOW, MoveTarget.EVERY_OPPONENT, 20, 99, 0, 13));
        registerMove(new AttackBase("selfdestruct", MoveType.ALMIGHTY, MoveTarget.EVERYONE, 200, 100, 0, 0));
        registerMove(new AttackBase("megido", MoveType.ALMIGHTY, MoveTarget.SINGLE_OPPONENT, 120, 95, 0, 15));
        registerMove(new AttackBase("megidola", MoveType.ALMIGHTY, MoveTarget.SINGLE_OPPONENT, 180, 95, 0, 24));
        registerMove(new AttackBase("megidolaon", MoveType.ALMIGHTY, MoveTarget.SINGLE_OPPONENT, 210, 95, 0, 38));
        registerMove(new AttackBase("deca", MoveType.WITHER, MoveTarget.SINGLE_OPPONENT, 20, 100, 5, 4));
        registerMove(new AttackBase("decaleo", MoveType.WITHER, MoveTarget.SINGLE_OPPONENT, 80, 100, 7, 8));
        registerMove(new AttackBase("decadyne", MoveType.WITHER, MoveTarget.SINGLE_OPPONENT, 200, 100, 10, 12));
        registerMove(new AttackBase("witherblast", MoveType.WITHER, MoveTarget.EVERY_OPPONENT, 60, 60, 20, 21));
        registerMove(new SupportBase("tarukaja", MoveTarget.SINGLE_ALLY, 8).addEffect(FighterEffect.ATTACK_UP));
        registerMove(new SupportBase("teleport", MoveTarget.SELF, 6).addEffect(FighterEffect.AGILITY_UP));
        registerMove(new AilmentBase("makajama", MoveTarget.SINGLE_OPPONENT, 5).addEffect(new AttributeEffect(FighterEffect.FORGET, EffectOdds.HIGH)));

        //boss
        registerMove(new AilmentBase("sew", MoveTarget.EVERY_OPPONENT, 26).addEffect(new AttributeEffect(FighterEffect.SEWN, EffectOdds.MEDIUM)));
        registerMove(new AttackBase("void", MoveType.END, MoveTarget.SINGLE_OPPONENT, 250, 95, 10, 24));
    }

    public static void registerMove(MoveBase move)
    {
        moveList.put(move.getStringID(), move);
        String output = move.getStringID() + " was added to the move database.";
        System.out.println(output);
    }

    public static MoveBase getMove(String id)
    {
        return moveList.getOrDefault(id, NULL);
    }

    public static Set<String> getAllMoveIDs() {
        return moveList.keySet();
    }

    /*public static MoveBase getMove(int id)
    {
        Set<String> keys = moveList.keySet();
        for(String key: keys)
        {
            if(id == moveList.get(key).getPrivateID())
                return moveList.get(key);
        }
        return null;
    }*/
}