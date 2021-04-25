package net.dohaw.blackclover.runnable.particle;

import lombok.Setter;
import net.dohaw.blackclover.util.MathHelper;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

public class TornadoParticleRunner extends CircleParticleRunner{

    @Setter
    protected double verticalPointSpread = 0.2;

    @Setter
    protected int verticalPoints;

    private int numIteration = 0;
    private boolean goesRight;

    public TornadoParticleRunner(Entity entity, Particle.DustOptions data, boolean isYIncreasing, double radius, boolean goesRight) {
        super(entity, data, isYIncreasing, radius);
        this.maxYAdditive = 3;
        this.verticalPoints = 10;
        this.goesRight = goesRight;
    }

    @Override
    public void run(){

        if(areEntitiesValid()){

            doYIncreaseCheck();

            if(numIteration >= POINTS){
                numIteration = 0;
            }

            double yAdditive2 = yAdditive;
            for (int i = 0; i < verticalPoints; i++) {
                double angle = MathHelper.angle(numIteration, POINTS);
                if(goesRight){
                    angle *= -1;
                }
                Location point = entity.getLocation().clone().add(radius * Math.sin(angle), yAdditive2, radius * Math.cos(angle));
                entity.getWorld().spawnParticle(particle, point, 6, 0, 0, 0, 0, data);
                yAdditive2 += verticalPointSpread;
            }
            numIteration++;

        }

    }

}
