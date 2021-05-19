package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import net.dohaw.blackclover.event.PortalThresholdCrossEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

public abstract class Portal {

    protected final double PORTAL_WIDTH, PORTAL_HEIGHT;
    protected final Location PORTAL_START_LOCATION;

    private BukkitTask portalDrawer, portalEnterChecker;

    protected final int NUM_HORIZONTAL_POINTS = 10;
    protected final int NUM_VERTICAL_POINTS = 15;

    protected final Particle.DustOptions DUST_OPTIONS;

    protected final double HORIZONTAL_DISTANCE_BETWEEN_POINTS, VERTICAL_DISTANCE_BETWEEN_POINTS;

    /**
     * Constructor used for the Portals spell
     */
    public Portal(org.bukkit.Location bottomLeftCorner, PortalSpell portalSpell, boolean isFirstPortal){
        this.PORTAL_START_LOCATION = bottomLeftCorner;
        this.PORTAL_WIDTH = portalSpell.getWidthPortal();
        this.PORTAL_HEIGHT = portalSpell.getHeightPortal();
        this.DUST_OPTIONS = isFirstPortal ? new Particle.DustOptions(Color.AQUA, 1) : new Particle.DustOptions(Color.ORANGE, 1);
        this.HORIZONTAL_DISTANCE_BETWEEN_POINTS = PORTAL_WIDTH / NUM_HORIZONTAL_POINTS;
        this.VERTICAL_DISTANCE_BETWEEN_POINTS = PORTAL_HEIGHT / NUM_VERTICAL_POINTS;
        this.portalDrawer = initPortalDrawer();
        initPortalEnterChecker();
    }

    public Portal(org.bukkit.Location bottomLeftCorner, PortalSpell portalSpell, Particle.DustOptions dustOptions){
        this.PORTAL_START_LOCATION = bottomLeftCorner;
        this.PORTAL_WIDTH = portalSpell.getWidthPortal();
        this.PORTAL_HEIGHT = portalSpell.getHeightPortal();
        this.DUST_OPTIONS = dustOptions;
        this.HORIZONTAL_DISTANCE_BETWEEN_POINTS = PORTAL_WIDTH / NUM_HORIZONTAL_POINTS;
        this.VERTICAL_DISTANCE_BETWEEN_POINTS = PORTAL_HEIGHT / NUM_VERTICAL_POINTS;
        initPortalDrawer();
        initPortalEnterChecker();
    }

    protected abstract BukkitTask initPortalDrawer();

    protected abstract BoundingBox createBoundingBox();

    protected abstract void drawVerticalPoints(Location startLocation);

    public void teleport(Entity entity){
        Location middleOfPortal = LocationUtil.getAbsoluteLocationToRight(PORTAL_START_LOCATION, PORTAL_WIDTH /  2).add(0, PORTAL_HEIGHT /  2, 0);
        entity.teleport(middleOfPortal);
    }

    protected void drawHorizontalPoints(Location startLocation){

        Location currentParticleLocation = startLocation.clone();
        for (int i = 0; i < NUM_HORIZONTAL_POINTS; i++) {
            SpellUtils.spawnParticle(currentParticleLocation, Particle.REDSTONE, DUST_OPTIONS, 10, 0.3f, 0.3f, 0.3f);
            currentParticleLocation = LocationUtil.getAbsoluteLocationToRight(currentParticleLocation, HORIZONTAL_DISTANCE_BETWEEN_POINTS);
        }

    }

    public void initPortalEnterChecker(){
        BoundingBox boundingBox = createBoundingBox();
        this.portalEnterChecker = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            for(Entity entity : PORTAL_START_LOCATION.getWorld().getNearbyEntities(boundingBox)){
                Bukkit.getPluginManager().callEvent(new PortalThresholdCrossEvent<>(entity, this));
            }
        }, 0L, 3L);
    }

    public void stopPortal(){
        portalEnterChecker.cancel();
        portalDrawer.cancel();
    }

    public Location getBottomLeftCorner() {
        return PORTAL_START_LOCATION.clone();
    }

    public BukkitTask getPortalDrawer() {
        return portalDrawer;
    }

    public BukkitTask getPortalEnterChecker() {
        return portalEnterChecker;
    }

}
