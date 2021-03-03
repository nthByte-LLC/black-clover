package net.dohaw.blackclover.runnable.particle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class CircleParticleRunner extends BukkitRunnable {

    protected final int POINTS = 30;
    protected double yIncrease = 0.1;
    protected double maxY = 1;

    protected Entity entity;
    protected Particle particle;
    protected boolean isYIncreasing;
    protected int radius;
    protected double yAdditive = 0;

    protected Particle.DustOptions data;

    public CircleParticleRunner(Entity entity, Particle particle, boolean isYIncreasing, int radius){
        this.entity = entity;
        this.particle = particle;
        this.isYIncreasing = isYIncreasing;
        this.radius = radius;
    }

    public CircleParticleRunner(Entity entity, Particle particle, Particle.DustOptions data, boolean isYIncreasing, int radius){
        this.entity = entity;
        this.particle = particle;
        this.isYIncreasing = isYIncreasing;
        this.radius = radius;
        this.data = data;
    }

    @Override
    public final void run(){
        doYIncreaseCheck();
        for (int i = 0; i < POINTS; i++) {
            doParticleIteration(i);
        }
    }

    protected double angle(int i){
        return 2 * Math.PI * i / POINTS;
    }

    protected void doYIncreaseCheck(){
        if(isYIncreasing){
            if(yAdditive >= maxY){
                yAdditive = 0;
            }else{
                yAdditive += yIncrease;
            }
        }
    }

    protected void doParticleIteration(int iteration){
        double angle = angle(iteration);
        Location point = entity.getLocation().clone().add(radius * Math.sin(angle), yAdditive, radius * Math.cos(angle));
        if(data != null){
            entity.getWorld().spawnParticle(particle, point, 6, 0, 0, 0, 0, data);
        }else{
            entity.getWorld().spawnParticle(particle, point, 6, 0, 0, 0, 0);
        }
    }

}
