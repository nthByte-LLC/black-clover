package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.EntityEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HealthStealer extends BukkitRunnable {

    private Player stealer;
    private LivingEntity le;
    private double stealingAmount;
    private BukkitTask particleLineDrawer;

    public HealthStealer(Player stealer, LivingEntity le, double stealAmount, BukkitTask particleLineDrawer){
        this.stealer = stealer;
        this.le = le;
        this.stealingAmount = stealAmount;
        this.particleLineDrawer = particleLineDrawer;
    }

    @Override
    public void run() {
        double alteredHealth = SpellUtils.alterHealth(le, -stealingAmount);
        SpellUtils.alterHealth(stealer, alteredHealth);
        SpellUtils.playSound(le, Sound.ENTITY_PLAYER_HURT);
        le.playEffect(EntityEffect.HURT);
        SpellUtils.spawnParticle(le, Particle.HEART, 10, 0.5f, 0.5f, 0.5f);
        if(le.getHealth() <= 0){
            particleLineDrawer.cancel();
            cancel();
        }
    }

}
