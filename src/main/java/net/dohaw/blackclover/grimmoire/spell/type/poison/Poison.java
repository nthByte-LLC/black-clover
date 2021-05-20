package net.dohaw.blackclover.grimmoire.spell.type.poison;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class Poison extends CastSpellWrapper {

    private int castDistance;

    private double slownessDuration, poisonDuration;
    private int slownessLevel, poisonLevel;

    public Poison(GrimmoireConfig grimmoireConfig) {
        super(SpellType.POISON, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity targetEntity = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, targetEntity)){

            LivingEntity target = (LivingEntity) targetEntity;

            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int) (poisonDuration * 20), poisonLevel - 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (slownessDuration * 20), slownessLevel - 1));

            Particle.DustOptions dustOptions = new Particle.DustOptions(BukkitColor.CYAN, 1.5f);
            Particle.DustOptions dustOptions2 = new Particle.DustOptions(BukkitColor.VIOLET, 2);

            SpellUtils.playSound(target, Sound.BLOCK_DISPENSER_DISPENSE);
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {

                if(target.hasPotionEffect(PotionEffectType.POISON)){
                    SpellUtils.spawnParticle(target.getLocation(), Particle.REDSTONE, dustOptions, 30, 1, 1, 1);
                }

                if(target.hasPotionEffect(PotionEffectType.SLOW)){
                    SpellUtils.spawnParticle(target.getLocation(), Particle.REDSTONE, dustOptions2, 30, 0.5f, 0.5f, 0.5f);
                }

            }, 20L, 20L);

            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, task::cancel, (long) (poisonDuration * 20));

            return true;
        }

        return false;

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.poisonDuration = grimmoireConfig.getDoubleSetting(KEY, "Poison Duration");
        this.slownessDuration = grimmoireConfig.getDoubleSetting(KEY, "Slowness Duration");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.poisonLevel = grimmoireConfig.getIntegerSetting(KEY, "Poison Level");
        this.slownessLevel = grimmoireConfig.getIntegerSetting(KEY, "Slowness Level");
    }

}
