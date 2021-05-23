package com.sekai.personabattlemod.util;

import com.sekai.personabattlemod.battle.BattleArena;
import com.sekai.personabattlemod.battle.BattleManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class BattleUtil {
    public static BattleArena getArena(ServerPlayerEntity source, LivingEntity target) {
        return new BattleArena();
    }
}
