package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.type.wind.Pull;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class PullRunner extends BukkitRunnable {

    private Player caster;
    private Pull spell;
    private int numPulls = 0;

    private TornadoParticleRunner tpr1, tpr2;

    public PullRunner(Player caster, Pull spell){
        this.caster = caster;
        this.spell = spell;
        startTornado();
    }

    @Override
    public void run() {

        SpellUtils.playSound(caster, Sound.ENTITY_ENDER_DRAGON_FLAP);

        double radius = spell.getRadius();
        Collection<Entity> nearbyEntities = caster.getNearbyEntities(radius, radius, radius);
        for(Entity en : nearbyEntities){
            if(en instanceof LivingEntity){
                LivingEntity livingEntity = (LivingEntity) en;
                pull(livingEntity, caster.getLocation());
            }
        }

        numPulls++;
        if(numPulls == spell.getNumPulls()){
            cancel();
        }

    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        this.tpr2.cancel();
        this.tpr1.cancel();
    }

    public void pull(LivingEntity willBePulled, Location pullFrom) {
        willBePulled.setVelocity(pullFrom.toVector().subtract(willBePulled.getLocation().toVector()).normalize().multiply(spell.getForceMultiplier()));
    }

    private void startTornado(){

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1);
        this.tpr1 = new TornadoParticleRunner(caster, dustOptions, true, 1.5, true);
        this.tpr2 = new TornadoParticleRunner(caster, dustOptions, true, 1.5, false);

        tpr1.runTaskTimer(Grimmoire.instance, 0L, 3L);
        tpr2.runTaskTimer(Grimmoire.instance, 0L, 3L);

    }

}
