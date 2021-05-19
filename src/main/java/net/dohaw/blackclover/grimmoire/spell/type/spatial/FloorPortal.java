package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

public class FloorPortal extends Portal{

    public FloorPortal(Location bottomLeftCorner, PortalSpell portalSpell, Particle.DustOptions dustOptions) {
        super(bottomLeftCorner, portalSpell, dustOptions);
    }

    @Override
    protected BukkitTask initPortalDrawer() {
        return new BukkitRunnable(){
            @Override
            public void run() {
                drawHorizontalPoints(getBottomLeftCorner());
                drawVerticalPoints(getBottomLeftCorner());
                drawHorizontalPoints(LocationUtil.getLocationInFront(getBottomLeftCorner(), PORTAL_HEIGHT));
                drawVerticalPoints(LocationUtil.getAbsoluteLocationToRight(getBottomLeftCorner(), PORTAL_WIDTH));
            }
        }.runTaskTimer(Grimmoire.instance, 0L, 5L);
    }

    @Override
    protected BoundingBox createBoundingBox() {

        Location firstBoundingBoxCorner = PORTAL_START_LOCATION.clone().subtract(0, 0.5, 0);

        Location pointForward = PORTAL_START_LOCATION.clone().add(0, 0.5, 0);
        Location pointForwardRight = LocationUtil.getLocationToRight(pointForward, PORTAL_WIDTH);
        Location secondBoundingBoxCorner = LocationUtil.getLocationInFront(pointForwardRight, PORTAL_HEIGHT);

        return BoundingBox.of(firstBoundingBoxCorner, secondBoundingBoxCorner);
    }

    @Override
    protected void drawVerticalPoints(Location startLocation){

        Location currentParticleLocation = startLocation.clone();
        for (int i = 0; i < NUM_VERTICAL_POINTS; i++) {
            SpellUtils.spawnParticle(currentParticleLocation, Particle.REDSTONE, DUST_OPTIONS, 10, 0.3f, 0.3f, 0.3f);
            currentParticleLocation = LocationUtil.getLocationInFront(currentParticleLocation, VERTICAL_DISTANCE_BETWEEN_POINTS);
        }

    }

}
