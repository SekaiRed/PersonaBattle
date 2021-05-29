package com.sekai.personabattlemod.battle;

import com.sekai.personabattlemod.battle.persona.IPersona;
import com.sekai.personabattlemod.capabilities.WildCardProvider;
import com.sekai.personabattlemod.config.PersonaConfig;
import com.sekai.personabattlemod.packets.PacketClientInitBattle;
import com.sekai.personabattlemod.util.BattleUtil;
import com.sekai.personabattlemod.util.PacketHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.*;
import java.util.stream.Collectors;

public class BattleManager {
    public static BattleManager instance;
    HashSet<LivingEntity> entitiesInBattle = new HashSet<>();
    HashMap<UUID, BattleInstance> battles = new HashMap<>();

    public BattleManager() {
        instance = this;
    }

    public void createRegularBattle(ServerPlayerEntity source, LivingEntity target) {
        //WildCard wc = source.getCapability(WildCardProvider.WC_CAP, null).orElse(null);
        /*boolean failed = true;
        //source.getCapability(WildCardProvider.WC_CAP).
        final WildCard wc = source.getCapability(WildCardProvider.WC_CAP, null).orElse(null);
        //source.getCapability(WildCardProvider.WC_CAP).ifPresent(temp -> {
        //    wc = temp;
        //});
        if(wc == null)
            return null;
        FighterInstance playerFighter = new FighterInstance();
        List<LivingEntity> foes = BattleUtil.getEnemies(target, PersonaConfig.battleRadius);
        for (LivingEntity foe : foes) {

        }*/

        source.getCapability(WildCardProvider.WC_CAP).ifPresent(wc -> {
            List<FighterInstance> playerSide = new LinkedList<>();
            List<FighterInstance> enemySide = new LinkedList<>();

            FighterInstance playerFighter = new FighterInstance(wc, source);
            playerSide.add(playerFighter);

            //FighterInstance enemyFighter = new FighterInstance(BattleUtil.getStats(target), target);
            //enemySide.add(enemyFighter);

            BattleUtil.getEnemies(target, PersonaConfig.battleRadius).forEach(entity -> {
                enemySide.add(new FighterInstance(BattleUtil.getStats(entity), entity));
            });

            createBattle(playerSide, enemySide, BattleUtil.getArena(source, target));
        });

        /*UUID uniqueKey = UUID.randomUUID();
        BattleInstance battle = new BattleInstance(uniqueKey, source, target, BattleUtil.getArena(source, target));

        if(!battle.validate())
            return null;

        battles.put(uniqueKey, battle);

        entitiesInBattle.add(source);
        entitiesInBattle.add(target);

        initBattle(battle);

        debugLogging();

        return battle;*/
    }

    private void createBattle(List<FighterInstance> playerSide, List<FighterInstance> enemySide, BattleArena arena) {
        UUID uniqueKey = UUID.randomUUID();

        //can only add entities that are not within a battle
        playerSide = playerSide.stream().filter(fighter -> !entitiesInBattle.contains(fighter.getEntity())).collect(Collectors.toList());
        enemySide = enemySide.stream().filter(fighter -> !entitiesInBattle.contains(fighter.getEntity())).collect(Collectors.toList());

        BattleInstance battle = new BattleInstance(uniqueKey, playerSide, enemySide, arena);
        battle.playerSide.forEach(fighter -> entitiesInBattle.add(fighter.getEntity()));
        battle.enemySide.forEach(fighter -> entitiesInBattle.add(fighter.getEntity()));

        if(!battle.validate())
            return;

        battles.put(uniqueKey, battle);

        initBattle(battle);

        debugLogging();
    }

    private void debugLogging() {
        System.out.println("Currently hosting " + battles.size() + " battles.");
        for(BattleInstance battle : battles.values()) {
            String playerSide = "On playerSide there is";
            for(FighterInstance fighter : battle.playerSide)
                playerSide += " " + fighter.entity.getName().getString();
            System.out.println(playerSide);

            String enemySide = "On enemySide there is";
            for(FighterInstance fighter : battle.enemySide)
                enemySide += " " + fighter.entity.getName().getString();
            System.out.println(enemySide);
            System.out.println("================");
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
    private void initBattle(BattleInstance battle) {
        for(FighterInstance player : battle.getAllClients())
            PacketHandler.NET.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player.getEntity()), new PacketClientInitBattle(battle));
    }

    public static void clear() {
        instance.battles.clear();
        instance.entitiesInBattle.clear();
    }

    @SubscribeEvent
    public void livingAttackEvent(LivingAttackEvent event) {
        if(!(event.getSource().getTrueSource() instanceof ServerPlayerEntity) || event.getSource().getTrueSource().getEntityWorld().isRemote)
            return;

        System.out.println(PersonaConfig.battleRadius);

        createRegularBattle((ServerPlayerEntity) event.getSource().getTrueSource(), event.getEntityLiving());
    }

    @SubscribeEvent
    public void serverTickEvent(TickEvent.ServerTickEvent event) {

    }

    public BattleInstance getBattle(UUID uniqueKey) {
        return battles.get(uniqueKey);
    }

    public void endBattle(UUID uniqueKey) {
        BattleInstance battle = battles.get(uniqueKey);
        for(FighterInstance fighter : battle.playerSide)
            entitiesInBattle.remove(fighter.entity);
        for(FighterInstance fighter : battle.enemySide)
            entitiesInBattle.remove(fighter.entity);
        battles.remove(uniqueKey);
    }

    public class BattleInstance {
        //todo probably need to make a state machine to track what is happening like when waiting for a client reply or executing an action and even just waiting
        private final UUID uniqueKey;
        public List<FighterInstance> playerSide;
        public List<FighterInstance> enemySide;

        public BattleArena arena;

        BattleInstance(UUID uniqueKey, List<FighterInstance> playerSide, List<FighterInstance> enemySide, BattleArena arena) {
            this.uniqueKey = uniqueKey;
            this.playerSide = playerSide;
            this.enemySide = enemySide;
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

        public UUID getUniqueKey() {
            return uniqueKey;
        }

        //todo maybe store all clients locally in BattleInstance instead of computing it every single time, it's gonna change rarely if ever

        public void addAllies(List<FighterInstance> allies) {
            playerSide.addAll(allies);
        }
        public void addFoes(List<FighterInstance> foes) {
            enemySide.addAll(foes);
        }

        //public ??? getHighestAgility();
    }

    public class FighterInstance {
        IPersona stat;
        LivingEntity entity;
        private final boolean isPlayer;

        private FighterInstance(IPersona stat, LivingEntity entity) {
            this.stat = stat;
            this.entity = entity;
            isPlayer = entity instanceof ServerPlayerEntity;
        }

        public IPersona getStat() {
            return stat;
        }

        public LivingEntity getEntity() {
            return entity;
        }

        public boolean isPlayer() {
            return isPlayer;
        }

        public ITextComponent getDisplayName() {
            return getEntity().getDisplayName();
        }
    }
}