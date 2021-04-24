package net.dohaw.blackclover.runnable.particle;

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class CircleParticleRunner extends EntityRunner {

    protected final int POINTS = 30;

    @Setter
    public double yIncrease = 0.1;

    @Setter
    public double maxYAdditive = 1;

    protected Entity entity;
    protected Particle particle;
    protected boolean isYIncreasing;
    protected double radius;
    protected double yAdditive = 0;

    // If you want the circle to start higher up, use this.
    @Setter
    protected double startYAdditive = 0;

    protected Particle.DustOptions data;

    /*
        For when you don't want to use something with dust options like redstone
     */
    public CircleParticleRunner(Entity entity, Particle particle, boolean isYIncreasing, double radius){
        super(entity);
        this.entity = entity;
        this.particle = particle;
        this.isYIncreasing = isYIncreasing;
        this.radius = radius;
    }

    /*
        Primarily used for redstone particles (To my knowledge)
     */
    public CircleParticleRunner(Entity entity, Particle.DustOptions data, boolean isYIncreasing, double radius){
        super(entity);
        this.entity = entity;
        this.particle = Particle.REDSTONE;
        this.isYIncreasing = isYIncreasing;
        this.radius = radius;
        this.data = data;
    }

    @Override
    public void run(){
        if(areEntitiesValid()){
            doYIncreaseCheck();
            for (int i = 0; i < POINTS; i++) {
                doParticleIteration(i);
            }
        }
    }

    protected double angle(int i){
        return 2 * Math.PI * i / POINTS;
    }

    protected void doYIncreaseCheck(){
        if(isYIncreasing){
            if(yAdditive >= maxYAdditive){
                yAdditive = 0;
            }else{
                if(yAdditive + yIncrease > maxYAdditive){
                    yAdditive = 0;
                }else{
                    yAdditive += yIncrease;
                }
            }
        }
    }

    protected void doParticleIteration(int iteration){
        double angle = angle(iteration);
        Location point = entity.getLocation().clone().add(0, startYAdditive, 0).add(radius * Math.sin(angle), yAdditive, radius * Math.cos(angle));
        if(data != null){
            entity.getWorld().spawnParticle(particle, point, 6, 0, 0, 0, 0, data);
        }else{
            entity.getWorld().spawnParticle(particle, point, 6, 0, 0, 0, 0);
        }
    }

}
