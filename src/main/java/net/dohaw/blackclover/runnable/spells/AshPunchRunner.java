package net.dohaw.blackclover.runnable.spells;

import lombok.NonNull;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class AshPunchRunner extends BukkitRunnable {

    // Adjusts how high the particle ends.
    private final double TARGET_Y_ADDITIVE = 2;
    private final double TARGET_HORIZ_ADDITIVE = 0.8;
    private final double NUM_PARTICLE_POINTS = 10;

    // how much you need to add to either the horizontal or y to make each particle evenly spread out.
    private final double Y_ADDITIVE = TARGET_Y_ADDITIVE / NUM_PARTICLE_POINTS;
    private final double HORIZ_ADDITIVE = TARGET_HORIZ_ADDITIVE / NUM_PARTICLE_POINTS;

    // what the additives start as (horiz gets subtracted since the particles come closer to the player. y gets added because the particle is going higher to simulate an uppercut)
    private double currentYAdditive = 0;
    // added something so that it starts slightly before it reaches the player's head.
    private double currentHorizAdditive = TARGET_HORIZ_ADDITIVE + 0.35;

    private LivingEntity target;
    private PlayerData caster;
    private double damage;

    public AshPunchRunner(PlayerData caster, @NonNull LivingEntity target, double damage){
        this.target = target;
        this.caster = caster;
        this.damage = damage;
    }

    /*
        Sort of creates a punch line (3 of them side-by-side)
     */
    @Override
    public void run() {

        Location locationInFrontTarget = LocationUtil.getLocationInFront(target, currentHorizAdditive);

        Location particleLocation1 = locationInFrontTarget.clone().add(0, currentYAdditive, 0);
        Location particleLocation2 = LocationUtil.getLocationToLeft(particleLocation1, 0.1);
        Location particleLocation3 = LocationUtil.getLocationToRight(particleLocation1, 0.1);

        Particle.DustOptions dustOptions = new Particle.DustOptions(BukkitColor.DARK_GREY, 0.5f);
        SpellUtils.spawnParticle(particleLocation1, Particle.REDSTONE, dustOptions, 10, 0, 0, 0);
        SpellUtils.spawnParticle(particleLocation2, Particle.REDSTONE, dustOptions, 10, 0, 0, 0);
        SpellUtils.spawnParticle(particleLocation3, Particle.REDSTONE, dustOptions, 10, 0, 0, 0);

        currentYAdditive += Y_ADDITIVE;
        currentHorizAdditive -= HORIZ_ADDITIVE;

        if(currentYAdditive >= TARGET_Y_ADDITIVE){

            SpellUtils.playSound(target, Sound.ENTITY_GENERIC_EXPLODE);
            SpellUtils.spawnParticle(target, Particle.EXPLOSION_NORMAL, 10, 1, 1, 1);

            // little bit of knockback
            Vector velocity = target.getLocation().getDirection();
            velocity.multiply(-0.5);
            velocity.setY(0.33333);
            target.setVelocity(velocity);

            SpellUtils.doSpellDamage(target, caster.getPlayer(), SpellType.ASH_PUNCH, damage);

            caster.stopSpellRunnables(SpellType.ASH_PUNCH);
        }

    }

}
