package net.dohaw.blackclover.runnable.particle;

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

public class TornadoParticleRunner extends CircleParticleRunner{

    @Setter
    protected int horizontalPoints;

    @Setter
    protected double horizontalPointSpread = 0.2;

    public TornadoParticleRunner(Entity entity, Particle particle, Particle.DustOptions data, boolean isYIncreasing, int radius) {
        super(entity, particle, data, isYIncreasing, radius);
        this.maxY = 3;
        this.horizontalPoints = 10;
    }

    @Override
    protected void doParticleIteration(int iteration) {
        doYIncreaseCheck();
        double angle = angle(iteration);
        double yAdditive2 = yAdditive;
        for (int i = 0; i < horizontalPoints; i++) {
            Location point = entity.getLocation().clone().add(radius * Math.sin(angle), yAdditive2, radius * Math.cos(angle));
            entity.getWorld().spawnParticle(particle, point, 6, 0, 0, 0, 0, data);
            yAdditive2 -= horizontalPointSpread;
        }
    }

}
