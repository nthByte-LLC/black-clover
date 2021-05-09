package net.dohaw.blackclover.runnable.particle;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.util.MathHelper;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class TornadoParticleRunner extends CircleParticleRunner{

    private List<Location> particleLocations = new ArrayList<>();

    private final double START_RADIUS;

    private double iteration = 0;

    // Whether the tornado goes right or left.
    private boolean goesRight;

    public TornadoParticleRunner(Entity entity, Particle.DustOptions data, double startRadius, boolean goesRight) {
        super(entity, data, true, startRadius);
        this.yAdditive = 0.5;
        this.points = 15;
        this.goesRight = goesRight;
        this.maxYAdditive = 5;
        this.START_RADIUS = startRadius;
    }

    @Override
    public void run(){

        if(areEntitiesValid()){

            if(iteration == points){
                iteration = 0;
            }

            if(yAdditive >= maxYAdditive){
                yAdditive = 0.5;
                radius = START_RADIUS;
                particleLocations.clear();
            }

            double angle = MathHelper.angle(iteration, points);
            if(goesRight){
                angle *= -1;
            }
            Location entityLocation = entity.getLocation().clone();
            Location point = entityLocation.add(radius * Math.sin(angle), yAdditive, radius * Math.cos(angle));
            SpellUtils.spawnParticle(point, particle, data, 15, 0, 0,0);
            if(iteration % 4 == 0){
                for(Location loc : particleLocations){
                    SpellUtils.spawnParticle(loc, particle, data, 5, 0, 0,0);
                }
            }
            particleLocations.add(point);
            iteration++;
            yAdditive += 0.1;
            radius += 0.05;

        }

    }

    public List<Location> getParticleLocations() {
        return particleLocations;
    }

}
