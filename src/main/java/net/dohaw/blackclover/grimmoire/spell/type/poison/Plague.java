package net.dohaw.blackclover.grimmoire.spell.type.poison;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Gives the target poison as well as the hunger effect.
 */
public class Plague extends CastSpellWrapper {

    private double poisonDuration, hungerDuration;
    private int poisonLevel, hungerLevel;
    private int castDistance;

    public Plague(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PLAGUE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity targetInSight = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, targetInSight)){

            LivingEntity target = (LivingEntity) targetInSight;
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int) (poisonDuration * 20), poisonLevel - 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, (int) (hungerDuration * 20), hungerLevel - 1));

            BukkitTask hungerParticleSpawner = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
                if(target.hasPotionEffect(PotionEffectType.HUNGER)){
                    ThreadLocalRandom current = ThreadLocalRandom.current();
                    float randomSize = (float) current.nextDouble(1, 2.5);
                    float randomOffset = (float) current.nextDouble(0.5, 1);
                    Particle.DustOptions dustOptions = new Particle.DustOptions(BukkitColor.DARK_GREY, randomSize);
                    SpellUtils.spawnParticle(target.getLocation(), Particle.REDSTONE, dustOptions, 30, randomOffset, randomOffset, randomOffset);
                }
            }, 20L, 20L);

            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, hungerParticleSpawner::cancel, (long) (hungerDuration * 20));

            Grimmoire.POISON.startPoisonEffect(target);
            SpellUtils.playSound(target, Sound.ENTITY_HOGLIN_CONVERTED_TO_ZOMBIFIED);
            SpellUtils.spawnParticle(target, Particle.SPELL_MOB_AMBIENT, 30, 1, 1, 1);

            return true;

        }

        return false;
    }

    @Override
    public void prepareShutdown() { }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.poisonDuration = grimmoireConfig.getDoubleSetting(KEY, "Poison Duration");
        this.poisonLevel = grimmoireConfig.getIntegerSetting(KEY, "Poison Level");
        this.hungerDuration = grimmoireConfig.getDoubleSetting(KEY, "Hunger Duration");
        this.hungerLevel = grimmoireConfig.getIntegerSetting(KEY, "Hunger Level");
    }

}
