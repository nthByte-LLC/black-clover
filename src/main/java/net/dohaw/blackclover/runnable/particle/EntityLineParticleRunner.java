package net.dohaw.blackclover.runnable.particle;

import org.bukkit.Particle;
import org.bukkit.entity.Entity;

/**
 * Draws a line from one entity to another
 */
public class EntityLineParticleRunner extends LineParticleRunner {

    private Entity startEntity, target;

    public EntityLineParticleRunner(Entity startEntity, Entity target, Particle.DustOptions dustOptions, double spread){
        super(startEntity.getLocation(), target.getLocation(), dustOptions, spread);
        this.startEntity = startEntity;
        this.target = target;
    }

    @Override
    public void run() {

        if(startEntity.isValid() || target.isValid() || isWithinReasonableDistance()){
            cancel();
        }

        super.run();

    }

    public boolean isWithinReasonableDistance(){
        return startEntity.getLocation().distance(target.getLocation()) < 20;
    }

    @Override
    public void updateLocations(){
        this.start = startEntity.getLocation();
        this.end = target.getLocation();
    }

}
