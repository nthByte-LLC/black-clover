package net.dohaw.blackclover.runnable.particle;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Unlike lines, beams are something that are draw over a period of time. Not the whole thing at once like LineParticleRunner.
 * Beams slowly get drawn from the origin of the beam, to it's end location.
 */
public class BeamParticleRunner extends LineParticleRunner{

    /*
        Whether the beam should keep drawing itself
     */
    private boolean isPersistent;

    protected List<Location> particleLocations = new ArrayList<>();
    protected Entity entity;
    protected BukkitTask beamDrawer;

    public BeamParticleRunner(Entity start, Location end, Particle.DustOptions dustOptions, double spread, boolean isPersistent) {
        super(start.getLocation(), end, dustOptions, spread);
        this.isPersistent = isPersistent;
        this.entity = start;
        initBeamDrawer();
    }

    public BeamParticleRunner(Entity start, double distanceBeam, Particle.DustOptions dustOptions, double spread, boolean isPersistent){
        super(start.getLocation(), LocationUtil.getLocationInFront(start, distanceBeam).add(0,1 , 0), dustOptions, spread);
        this.isPersistent = isPersistent;
        this.entity = start;
        initBeamDrawer();
    }

    /**
     * We need the beam drawer to be a separate task so that it goes slower.
     * The BeamParticleRunner task itself will be running faster than the drawer in order to check to see who is in the beam itself.
     */
    public void initBeamDrawer(){
        this.beamDrawer = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            if(entity.isDead()){
                cancel();
            }
            Location particleLoc = getParticleLocation();
            boolean isWithinABlock = isCloseToEnd(particleLoc);
            boolean isClearPath = particleLoc.getBlock().getType() == Material.AIR;
            if(!isWithinABlock && isClearPath){
                particleLocations.add(particleLoc.clone());
                count += spread;
                particleLocations.forEach(loc -> {
                    SpellUtils.spawnParticle(loc, Particle.REDSTONE, dustOptions, 30, 0.3f, 0.3f, 0.3f);
                    doParticleLocationSpecifics(loc);
                });
            }else{
                if(isPersistent){
                    count = 0;
                    particleLocations.clear();
                }else{
                    doEndOfBeamSpecifics();
                    cancel();
                }
            }
        }, 0L, 5L);
    }

    @Override
    public void drawLine() { }

    /**
     * If a spell wanted to do something at the end of the beam, you'd do it here.
     */
    public void doEndOfBeamSpecifics(){ }

    /**
     * If the spell wanted to do some sort of check on each particle location, you'd do it here.
     */
    public void doParticleLocationSpecifics(Location particleLoc){}

    @Override
    public void updateLocations() {
        // could be null if we use the beam distance constructor
        if(entity != null){
            this.start = entity.getLocation();
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        System.out.println("STOPPING BEAM DRAWER");
        this.beamDrawer.cancel();
        super.cancel();
    }

}
