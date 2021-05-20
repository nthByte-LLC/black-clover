package net.dohaw.blackclover.grimmoire.spell.type.ash;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class AshBomb extends CastSpellWrapper {

    private int radius;
    private double durationBlindness;

    public AshBomb(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ASH_BOMB, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Collection<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
        SpellUtils.spawnParticle(player, Particle.FLASH, 10, 0, 0, 0);
        SpellUtils.playSound(player, Sound.ENTITY_ENDER_DRAGON_FLAP);
        for(Entity en : nearbyEntities){
            if(en instanceof LivingEntity){
                LivingEntity le = (LivingEntity) en;
                // re-applies blindness.
                if(le.hasPotionEffect(PotionEffectType.BLINDNESS)){
                    le.removePotionEffect(PotionEffectType.BLINDNESS);
                }

                le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int) (durationBlindness * 20), 0, false));

                /*
                    Blindness particles
                 */
                BukkitTask task = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
                    ThreadLocalRandom tlr = ThreadLocalRandom.current();
                    Location leLocation = le.getLocation();
                    double randXAdditive = tlr.nextDouble(-1, 1);
                    double randYAdditive = tlr.nextDouble(-1, 1);
                    double randZAdditive = tlr.nextDouble(-1, 1);
                    Location particleLocation = leLocation.clone().add(randXAdditive, randYAdditive, randZAdditive);
                    SpellUtils.spawnParticle(particleLocation, Particle.ASH, 30, 0.5f, 0.5f, 0.5f);
                }, 0L, 5L);

                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                    le.removePotionEffect(PotionEffectType.BLINDNESS);
                    task.cancel();
                }, (long) (durationBlindness * 20));

            }
        }

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getIntegerSetting(KEY, "Radius");
        this.durationBlindness = grimmoireConfig.getDoubleSetting(KEY, "Duration Blindness");
    }

}
