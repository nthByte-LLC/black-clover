package net.dohaw.blackclover.runnable.particle;

import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

public class HelixParticleRunner extends CircleParticleRunner {

    private boolean isOpposite;
    private double y;

    public HelixParticleRunner(Entity entity, Particle.DustOptions data, double radius, boolean isOpposite) {
        super(entity, data, true, radius);
        yAdditive = 0.05;
        this.y = entity.getLocation().getY();
        this.isOpposite = isOpposite;
    }

    public HelixParticleRunner(Location location, Particle.DustOptions data, double radius, boolean isOpposite){
        super(SpellUtils.invisibleArmorStand(location), data, true, radius);
        yAdditive = 0.05;
        this.y = entity.getLocation().getY();
        this.isOpposite = isOpposite;
    }

    @Override
    public void run() {
        doYIncreaseCheck();
        doParticleIteration(0);
    }

    @Override
    protected void doParticleIteration(int iteration) {

        if(yAdditive == 0){
            y = entity.getLocation().getY();
        }
        y += yAdditive;

        double tempY = y;
        if(isOpposite){
            tempY *= -1;
        }
        double x = radius * Math.cos(tempY);
        double z = radius * Math.sin(tempY);
        Location loc = entity.getLocation().add(x, 0, z);
        loc.setY(y);
        entity.getWorld().spawnParticle(particle, loc, 6, 0, 0, 0, 0, data);

    }

}
