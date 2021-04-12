package net.dohaw.blackclover.grimmoire.spell.type.anti;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Disable extends ActivatableSpellWrapper implements Listener {

    private double durationDisable;

    public Disable(GrimmoireConfig grimmoireConfig) {
        super(SpellType.DISABLE, grimmoireConfig);
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e){

        Entity eDamaged = e.getEntity();
        Entity eDamager = e.getDamager();
        if(eDamaged instanceof Player && eDamager instanceof Player){

            Player damaged = (Player) eDamaged;
            Player damager = (Player) eDamager;
            PlayerData damagerData = Grimmoire.instance.getPlayerDataManager().getData(damager.getUniqueId());
            if(damagerData.isSpellActive(SpellType.DISABLE)){

                SpellDamageEvent event = new SpellDamageEvent(KEY, 0,e.getEntity(), damager);
                Bukkit.getPluginManager().callEvent(event);
                if(!event.isCancelled()){

                    PlayerData damagedData = Grimmoire.instance.getPlayerDataManager().getData(damaged.getUniqueId());
                    damagedData.setCanCast(false);
                    SpellUtils.playSound(damaged, Sound.ITEM_SHIELD_BREAK);
                    SpellUtils.spawnParticle(damaged, Particle.SPELL_WITCH, 30, 0.5f, 0.5f, 0.5f);

                    Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                        damagedData.setCanCast(true);
                    }, (long) (durationDisable * 20));

                    double eventDamage = event.getDamage();
                    if(eventDamage != 0){
                        e.setDamage(eventDamage);
                    }

                }

            }
        }
    }


    @Override
    public void doRunnableTick(PlayerData caster) {

        Player player = caster.getPlayer();
        CircleParticleRunner particleRunner = new CircleParticleRunner(player, new Particle.DustOptions(BukkitColor.darkGrey, 1), true, 1);
        particleRunner.setMaxYAdditive(0.4);

        caster.addSpellRunnables(KEY, particleRunner.runTaskTimer(Grimmoire.instance, 0L, 10L));

    }

    @Override
    public void deactiveSpell(PlayerData caster) throws UnexpectedPlayerData {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.durationDisable = grimmoireConfig.getIntegerSetting(KEY, "Duration Disable");
    }

    @Override
    public void prepareShutdown() {

    }
}
