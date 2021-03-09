package net.dohaw.blackclover.runnable.particle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

public class HelixParticleRunner extends CircleParticleRunner {

    public HelixParticleRunner(Entity entity, Particle.DustOptions data, double radius) {
        super(entity, data, true, radius);
        yAdditive = 0.05;
    }

    @Override
    public void run() {
        doYIncreaseCheck();
        doParticleIteration(0);
    }

    @Override
    protected void doParticleIteration(int iteration) {
        Location entityLocation = entity.getLocation();
        double x = radius * Math.cos(yAdditive);
        double z = radius * Math.sin(yAdditive);
        Location loc = entityLocation.add(x, yAdditive, z);
        entity.getWorld().spawnParticle(particle, loc, 6, 0, 0, 0, 0, data);
    }

}
