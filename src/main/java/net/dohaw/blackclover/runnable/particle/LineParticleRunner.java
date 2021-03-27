package net.dohaw.blackclover.runnable.particle;

import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class LineParticleRunner extends BukkitRunnable {

    private Particle.DustOptions dustOptions;
    private Entity startEntity, target;
    private double spread;
    private double count = 0;

    public LineParticleRunner(Entity startEntity, Entity target, Particle.DustOptions dustOptions, double spread){
        this.startEntity = startEntity;
        this.target = target;
        this.dustOptions = dustOptions;
        this.spread = spread;
    }

    @Override
    public void run() {

        if(startEntity.isDead() || target.isDead()){
            cancel();
        }

        Location particleLoc = getParticleLocation();
        boolean isWithinABlock = particleLoc.distance(target.getLocation()) <= 1;
        while(!isWithinABlock){
            SpellUtils.spawnParticle(particleLoc, Particle.REDSTONE, dustOptions, 30, 0, 0, 0);
            count += spread;
            particleLoc = getParticleLocation();
            isWithinABlock = particleLoc.distance(target.getLocation()) <= 1;
        }
        count = 0;

    }

    private Location getParticleLocation(){
        Location start = startEntity.getLocation();
        Location end = target.getLocation();
        Vector dir = end.clone().subtract(start).toVector();
        Vector currentDir = dir.clone().multiply(count);
        return start.clone().add(currentDir);
    }

}
