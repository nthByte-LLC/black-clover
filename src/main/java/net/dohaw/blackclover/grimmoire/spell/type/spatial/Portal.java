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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

public class Portal {
    
    private final double PORTAL_WIDTH, PORTAL_HEIGHT;
    private final Location BOTTOM_LEFT_CORNER;

    private BukkitTask portalDrawer, portalEnterChecker;

    private final Particle.DustOptions DUST_OPTIONS;

    private final int NUM_HORIZONTAL_POINTS = 10;
    private final int NUM_VERTICAL_POINTS = 15;

    private final double HORIZONTAL_DISTANCE_BETWEEN_POINTS, VERTICAL_DISTANCE_BETWEEN_POINTS;

    private BoundingBox boundingBox;

    /**
     * Constructor used for the Portals spell
     */
    public Portal(Location bottomLeftCorner, double portalWidth, double portalHeight, boolean isFirstPortal){
        this.BOTTOM_LEFT_CORNER = bottomLeftCorner;
        this.PORTAL_WIDTH = portalWidth;
        this.PORTAL_HEIGHT = portalHeight;
        this.DUST_OPTIONS = isFirstPortal ? new Particle.DustOptions(Color.AQUA, 1) : new Particle.DustOptions(Color.ORANGE, 1);
        this.HORIZONTAL_DISTANCE_BETWEEN_POINTS = PORTAL_WIDTH / NUM_HORIZONTAL_POINTS;
        this.VERTICAL_DISTANCE_BETWEEN_POINTS = PORTAL_HEIGHT / NUM_VERTICAL_POINTS;
        initPortalDrawer();
        initPortalEnterChecker();
    }

    /**
     * Constructor used for the Teleport spell
     */
    public Portal(Location bottomLeftCorner, double portalWidth, double portalHeight, Particle.DustOptions dustOptions){
        this.BOTTOM_LEFT_CORNER = bottomLeftCorner;
        this.PORTAL_WIDTH = portalWidth;
        this.PORTAL_HEIGHT = portalHeight;
        this.DUST_OPTIONS = dustOptions;
        this.HORIZONTAL_DISTANCE_BETWEEN_POINTS = PORTAL_WIDTH / NUM_HORIZONTAL_POINTS;
        this.VERTICAL_DISTANCE_BETWEEN_POINTS = PORTAL_HEIGHT / NUM_VERTICAL_POINTS;
        initPortalDrawer();
        initPortalEnterChecker();
    }

    public Portal(Location bottomLeftCorner, PortalSpell portalSpell, Particle.DustOptions dustOptions){
        this.BOTTOM_LEFT_CORNER = bottomLeftCorner;
        this.PORTAL_WIDTH = portalSpell.getWidthPortal();
        this.PORTAL_HEIGHT = portalSpell.getHeightPortal();
        this.DUST_OPTIONS = dustOptions;
        this.HORIZONTAL_DISTANCE_BETWEEN_POINTS = PORTAL_WIDTH / NUM_HORIZONTAL_POINTS;
        this.VERTICAL_DISTANCE_BETWEEN_POINTS = PORTAL_HEIGHT / NUM_VERTICAL_POINTS;
        initPortalDrawer();
        initPortalEnterChecker();
    }
    
    public Location getBottomLeftCorner() {
        return BOTTOM_LEFT_CORNER.clone();
    }

    public BukkitTask getPortalDrawer() {
        return portalDrawer;
    }

    public BukkitTask getPortalEnterChecker() {
        return portalEnterChecker;
    }

    private void initPortalDrawer(){

        this.portalDrawer = new BukkitRunnable(){
            @Override
            public void run() {
               drawHorizontalPoints(getBottomLeftCorner());
               drawVerticalPoints(getBottomLeftCorner());
               drawHorizontalPoints(getBottomLeftCorner().add(0, PORTAL_HEIGHT, 0));
               drawVerticalPoints(LocationUtil.getAbsoluteLocationToRight(getBottomLeftCorner(), PORTAL_WIDTH));
            }
        }.runTaskTimer(Grimmoire.instance, 0L, 5L);

    }

    private void initPortalEnterChecker(){

        Location firstBoundingBoxCorner = LocationUtil.getAbsoluteLocationInBack(BOTTOM_LEFT_CORNER.clone(), 0.5);

        Location pointForward = LocationUtil.getAbsoluteLocationInFront(BOTTOM_LEFT_CORNER.clone(), 0.5);
        Location pointForwardRight = LocationUtil.getAbsoluteLocationToRight(pointForward.clone(), PORTAL_WIDTH);
        Location secondBoundingBoxCorner = pointForwardRight.add(0, PORTAL_HEIGHT, 0);

        this.boundingBox = BoundingBox.of(firstBoundingBoxCorner, secondBoundingBoxCorner);

        this.portalEnterChecker = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            for(Entity entity : firstBoundingBoxCorner.getWorld().getNearbyEntities(boundingBox)){
                Bukkit.getPluginManager().callEvent(new PortalThresholdCrossEvent(entity, this));
            }
        }, 0L, 3L);

    }

    private void drawHorizontalPoints(Location startLocation){

        Location currentParticleLocation = startLocation.clone();
        for (int i = 0; i < NUM_HORIZONTAL_POINTS; i++) {
            SpellUtils.spawnParticle(currentParticleLocation, Particle.REDSTONE, DUST_OPTIONS, 10, 0.3f, 0.3f, 0.3f);
            currentParticleLocation = LocationUtil.getAbsoluteLocationToRight(currentParticleLocation, HORIZONTAL_DISTANCE_BETWEEN_POINTS);
        }

    }

    private void drawVerticalPoints(Location startLocation){

        Location currentParticleLocation = startLocation.clone();
        for (int i = 0; i < NUM_VERTICAL_POINTS; i++) {
            SpellUtils.spawnParticle(currentParticleLocation, Particle.REDSTONE, DUST_OPTIONS, 10, 0.3f, 0.3f, 0.3f);
            currentParticleLocation = currentParticleLocation.add(0, VERTICAL_DISTANCE_BETWEEN_POINTS, 0);
        }

    }

    public void teleport(Entity entity){
        Location middleOfPortal = LocationUtil.getAbsoluteLocationToRight(BOTTOM_LEFT_CORNER, PORTAL_WIDTH /  2).add(0, PORTAL_HEIGHT /  2, 0);
        entity.teleport(middleOfPortal);
    }

}
