package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

/**
 * Defines a portal that stands up vertically like a nether portal
 */
public class StandingPortal extends Portal{

    /**
     * Constructor used for the Portals spell
     */
    public StandingPortal(Location bottomLeftCorner, PortalSpell spell, boolean isFirstPortal){
        super(bottomLeftCorner, spell, isFirstPortal);
    }

    public StandingPortal(Location bottomLeftCorner, PortalSpell portalSpell, Particle.DustOptions dustOptions){
        super(bottomLeftCorner, portalSpell, dustOptions);
    }

    public BukkitTask initPortalDrawer(){
        return new BukkitRunnable(){
            @Override
            public void run() {
               drawHorizontalPoints(getBottomLeftCorner());
               drawVerticalPoints(getBottomLeftCorner());
               drawHorizontalPoints(getBottomLeftCorner().add(0, PORTAL_HEIGHT, 0));
               drawVerticalPoints(LocationUtil.getAbsoluteLocationToRight(getBottomLeftCorner(), PORTAL_WIDTH));
            }
        }.runTaskTimer(Grimmoire.instance, 0L, 5L);
    }

    @Override
    protected BoundingBox createBoundingBox() {

        Location firstBoundingBoxCorner = LocationUtil.getAbsoluteLocationInBack(PORTAL_START_LOCATION.clone(), 0.5);

        Location pointForward = LocationUtil.getAbsoluteLocationInFront(PORTAL_START_LOCATION.clone(), 0.5);
        Location pointForwardRight = LocationUtil.getAbsoluteLocationToRight(pointForward.clone(), PORTAL_WIDTH);
        Location secondBoundingBoxCorner = pointForwardRight.add(0, PORTAL_HEIGHT, 0);

        return BoundingBox.of(firstBoundingBoxCorner, secondBoundingBoxCorner);

    }

    @Override
    protected void drawVerticalPoints(Location startLocation){

        Location currentParticleLocation = startLocation.clone();
        for (int i = 0; i < NUM_VERTICAL_POINTS; i++) {
            SpellUtils.spawnParticle(currentParticleLocation, Particle.REDSTONE, DUST_OPTIONS, 10, 0.3f, 0.3f, 0.3f);
            currentParticleLocation = currentParticleLocation.add(0, VERTICAL_DISTANCE_BETWEEN_POINTS, 0);
        }

    }

    public void teleport(Entity entity){
        Location middleOfPortal = LocationUtil.getAbsoluteLocationToRight(PORTAL_START_LOCATION, PORTAL_WIDTH /  2).add(0, PORTAL_HEIGHT /  2, 0);
        entity.teleport(middleOfPortal);
    }

}
