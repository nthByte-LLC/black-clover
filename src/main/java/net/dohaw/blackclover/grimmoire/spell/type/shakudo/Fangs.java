package net.dohaw.blackclover.grimmoire.spell.type.shakudo;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.FangsCastedEvent;
import net.dohaw.blackclover.event.PostCastSpellEvent;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.ShakudoPlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.ResponderFactory;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class Fangs extends CastSpellWrapper implements Listener {

    private double damageMultiplier;
    private int duration;

    public Fangs(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FANGS, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        if(pd instanceof ShakudoPlayerData){
            ShakudoPlayerData spd = (ShakudoPlayerData) pd;
            boolean fangsAreEnabled = spd.isFangsEnabled();
            if(!fangsAreEnabled){

                spd.setFangsEnabled(true);
                Bukkit.getPluginManager().callEvent(new FangsCastedEvent(spd));
                SpellUtils.playSound(spd.getPlayer(), Sound.ENTITY_WOLF_GROWL);

                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                    spd.setFangsEnabled(false);
                }, duration * 20);
                return true;

            }else{
                ResponderFactory rf = new ResponderFactory(pd.getPlayer());
                rf.sendMessage("You already have fangs enabled!");
            }
        } else {
            try {
                throw new UnexpectedPlayerData();
            } catch (UnexpectedPlayerData unexpectedPlayerData) {
                unexpectedPlayerData.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = (int) grimmoireConfig.getIntegerSetting(KEY, "Duration");
        this.damageMultiplier = grimmoireConfig.getIntegerSetting(KEY, "Damage Multiplier");
    }

    @EventHandler
    public void onFangsCast(FangsCastedEvent e){

        ShakudoPlayerData caster = e.getCaster();
        if(caster.isPackCalled()){
            caster.getPack().forEach(this::startWolfParticleRunner);
        }else if(caster.isSingularWolfSpawned()){
            startWolfParticleRunner(caster.getWolf());
        }

    }

    @EventHandler
    public void onWolfDamage(EntityDamageByEntityEvent e){
        Entity eDamager = e.getDamager();
        if(eDamager instanceof Wolf){
            Wolf wolf = (Wolf) eDamager;
            AnimalTamer tamer = wolf.getOwner();
            if(tamer != null){
                UUID tamerUUID = tamer.getUniqueId();
                PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(tamerUUID);
                if(pd != null){
                    if(pd instanceof ShakudoPlayerData){
                        ShakudoPlayerData spd = (ShakudoPlayerData) pd;
                        if(spd.isFangsEnabled()){

                            double newDamage = e.getDamage() * damageMultiplier;
                            SpellDamageEvent spellDamageEvent = new SpellDamageEvent(KEY, newDamage, e.getEntity(), pd.getPlayer());
                            Bukkit.getPluginManager().callEvent(spellDamageEvent);
                            if(!spellDamageEvent.isCancelled()){
                                e.setDamage(spellDamageEvent.getDamage());
                                SpellUtils.spawnParticle(wolf, Particle.CRIT_MAGIC, 10, 0.2f, 0.2f, 0.2f);
                                System.out.println("DOUBLED THE DAMAGE");
                            }

                        }
                    }else{
                        try {
                            throw new UnexpectedPlayerData();
                        } catch (UnexpectedPlayerData unexpectedPlayerData) {
                            unexpectedPlayerData.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSpawnWolves(PostCastSpellEvent e){

        SpellType spellType = e.getSpellCasted().getKEY();
        PlayerData pd = e.getPlayerData();
        if(pd instanceof ShakudoPlayerData){
            ShakudoPlayerData spd = (ShakudoPlayerData) e.getPlayerData();
            if(spd.isFangsEnabled()){
                if(spellType == SpellType.PACK){
                    if(spd.isPackCalled()){
                        spd.getPack().forEach(this::startWolfParticleRunner);
                    }
                }else if(spellType == SpellType.WILD_CALL){
                    if(spd.isSingularWolfSpawned()){
                        startWolfParticleRunner(spd.getWolf());
                    }
                }
            }
        }

    }

    public void startWolfParticleRunner(Wolf wolf){
        BukkitTask particleRunner = new CircleParticleRunner(wolf, new Particle.DustOptions(Color.WHITE, 2), false, 1).runTaskTimer(Grimmoire.instance, 0L, 3L);
        BukkitTask deathChecker = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            if(!wolf.isValid()){
                particleRunner.cancel();
            }
        }, 0L, 20L);
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            deathChecker.cancel();
            if(!particleRunner.isCancelled()){
                particleRunner.cancel();
            }
        }, duration * 20);
    }

}
