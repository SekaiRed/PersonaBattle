package com.sekai.personabattlemod.battle;

import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.client.gui.BetaProfileGui;
import com.sekai.personabattlemod.util.BattleUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;
import java.util.stream.Collectors;

public class BattleManager {
    public static BattleManager instance;
    HashSet<LivingEntity> entitiesInBattle = new HashSet<>();
    HashMap<UUID, BattleInstance> battles = new HashMap<>();

    public BattleManager() {
        instance = this;
    }

    public BattleInstance createBattle(ServerPlayerEntity source, LivingEntity target) {
        BattleInstance battle = new BattleInstance(source, target, BattleUtil.getArena(source, target));

        if(!battle.validate())
            return null;

        battles.put(UUID.randomUUID(), battle);

        entitiesInBattle.add(source);
        entitiesInBattle.add(target);

        initBattle(battle);

        debugLogging();

        return battle;
    }

    private void debugLogging() {
        System.out.println("Currently hosting " + battles.size() + " battles.");
        for(BattleInstance battle : battles.values()) {
            String playerSide = "On playerSide there is";
            for(FighterInstance fighter : battle.playerSide)
                playerSide += " " + fighter.entity.getName();
            System.out.println(playerSide);

            String enemySide = "On enemySide there is";
            for(FighterInstance fighter : battle.enemySide)
                enemySide += " " + fighter.entity.getName();
            System.out.println(enemySide);
        }
    }

    /*public BattleInstance createBattle(PlayerEntity source, LivingEntity target) {
        BattleInstance battle = new BattleInstance(source, target,BattleUtil.getArena(source, target));
        battle.addAllies(BattleUtil.getAllies(source, getWorld()));
        if(PersonaConfig.battleRadius > 0)
            battle.addFoes(BattleUtil.getFoes(PersonaConfig.battleRadius, target, getWorld()));

        battles.put(UUID.randomUUID(), battle);

        initBattle(battle);
    }*/

    /*
    todo copied straight from that old eclipse project, it's messy but there's the damage formula, need to adapt for this
    public int getSkillDamageAmount(FighterInfo att, FighterInfo def, String skill)
	{
		Stats attStat = att.getStats();
		Stats defStat = def.getStats();

		if(!(MoveDatabase.getMove(skill) instanceof AttackBase))
		{
			System.out.println("what the fuck you're not supposed to be here");
			return 0;
		}

		AttackBase move = (AttackBase)MoveDatabase.getMove(skill);

		int pow = 1; //attackers power
		int end = defStat.getStat(StatsType.ENDURANCE); //defenders endurance

		double mod = (double)attStat.getLevel() / (double)defStat.getLevel();

		if(move.isPhysical())
		{
			pow = attStat.getStat(StatsType.STRENGTH) * move.getPower();
		}
		else
		{
			pow = attStat.getStat(StatsType.MAGIC) * move.getPower();
		}

		double rng = 0.95D + Math.random() * 0.1D;

		double dmg = 5.0D * Math.sqrt((float)pow/(float)end) * mod * rng;

		return (int) Math.round(dmg);
	}
    */
    public void initBattle(BattleInstance battle) {
        /*List<PlayerEntity> players = battle.getClients();
        for(PlayerEntity player : players)
            NETWORK.send(PacketDistributor.PLAYER.with(player), new InitBattlePacket(battle));*/
    }

    @SubscribeEvent
    public void livingAttackEvent(LivingAttackEvent event) {
        if(!(event.getSource().getTrueSource() instanceof ServerPlayerEntity) || event.getSource().getTrueSource().getEntityWorld().isRemote)
            return;

        System.out.println(event.getSource().getTrueSource());

        createBattle((ServerPlayerEntity) event.getSource().getTrueSource(), event.getEntityLiving());
    }

    @SubscribeEvent
    public void serverTickEvent(TickEvent.ServerTickEvent event) {

    }

    private class BattleInstance {
        //todo probably need to make a state machine to track what is happening like when waiting for a client reply or executing an action and even just waiting
        public List<FighterInstance> playerSide = new LinkedList<>();
        public List<FighterInstance> enemySide = new LinkedList<>();

        public BattleArena arena;

        BattleInstance(ServerPlayerEntity source, LivingEntity target, BattleArena arena) {
            playerSide.add(new FighterInstance(source));
            enemySide.add(new FighterInstance(target));
            this.arena = arena;
        }

        public boolean validate() {
            return playerSide.size() > 0 && enemySide.size() > 0 && getAllClients().size() > 0;
        }

        /**Return a list of all client players present within either teams, useful for sending packets in all directions when an update is needed.
         * @return List of ServerPlayers
         */
        public List<FighterInstance> getAllClients() {
            List<FighterInstance> returnList = new LinkedList<>();
            returnList.addAll(playerSide.stream().filter(FighterInstance::isPlayer).collect(Collectors.toList()));
            returnList.addAll(enemySide.stream().filter(FighterInstance::isPlayer).collect(Collectors.toList()));
            return returnList;
        }
        //todo maybe store all clients locally in BattleInstance instead of computing it every single time, it's gonna change rarely if ever

        //public void addAllies(List<LivingEntity> allies)
        //public void addFoes(List<LivingEntity> foes)

        //public ??? getHighestAgility();
    }

    private class FighterInstance {
        LivingEntity entity;
        private final boolean isPlayer;

        private FighterInstance(LivingEntity entity) {
            this.entity = entity;
            isPlayer = entity instanceof ServerPlayerEntity;
        }

        public LivingEntity getEntity() {
            return entity;
        }

        public boolean isPlayer() {
            return isPlayer;
        }
    }
}