package net.dohaw.blackclover.runnable.particle;

import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Draws particles from one location to another.
 */
public class LineParticleRunner extends BukkitRunnable {

    protected Location start, end;
    protected Particle.DustOptions dustOptions;
    protected double spread, count;

    public LineParticleRunner(Location start, Location end, Particle.DustOptions dustOptions, double spread){
        this.start = start;
        this.end = end;
        this.dustOptions = dustOptions;
        this.spread = spread;
    }

    public void updateLocations(){}

    protected Location getParticleLocation(){
        Vector dir = end.clone().subtract(start).toVector();
        Vector currentDir = dir.clone().multiply(count);
        return start.clone().add(currentDir);
    }

    public void drawLine(){
        Location particleLoc = getParticleLocation();
        boolean isWithinABlock = particleLoc.distance(end) <= 1;
        while(!isWithinABlock){
            SpellUtils.spawnParticle(particleLoc, Particle.REDSTONE, dustOptions, 30, 0, 0, 0);
            count += spread;
            particleLoc = getParticleLocation();
            isWithinABlock = particleLoc.distance(end) <= 1;
        }
        count = 0;
    }

    @Override
    public void run() {
        updateLocations();
        drawLine();
    }

}
