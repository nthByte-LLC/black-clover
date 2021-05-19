package net.dohaw.blackclover.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class LocationUtil {

    public static Location getLocationInFront(Entity entity, double numBlocksInFront){
        return getLocationInFront(entity.getLocation(), numBlocksInFront);
    }

    /**
     * Absolute meaning, if the player were looking straight forward, it would get the block in front of the player.
     * This differs from #getLocationInFront because if you were to look up, it gets the location in front of you and pointing in that direction.
     * This method gets the location as if the player were looking straight forward, disregarding any pitch.
     */
    public static Location getAbsoluteLocationInFront(Entity entity, double numBlocksInFront){
        Location entityLocation = entity.getLocation();
        // makes them level-faced forward
        entityLocation.setPitch(0);
        return getLocationInFront(entityLocation, numBlocksInFront);
    }

    public static Location getAbsoluteLocationInFront(Location location, double numBlocksInFront){
        Location clone = location.clone();
        clone.setPitch(0);
        return getLocationInFront(clone, numBlocksInFront);
    }

    public static Location getAbsoluteLocationInBack(Location location, double numBlocksInBack) {
        Location clone = location.clone();
        clone.setPitch(0);
        return getLocationInFront(clone, numBlocksInBack * -1);
    }

    public static Location getLocationInFront(Location location, double numBlocksInFront){
        return location.clone().add(location.clone().getDirection().multiply(numBlocksInFront));
    }

    public static Location getLocationToRight(Location loc, double dist){
        Location rightDirectionLocation = loc.clone();
        // shifts direction to the right
        rightDirectionLocation.setYaw(rightDirectionLocation.getYaw() + 90);
        return loc.clone().add(rightDirectionLocation.getDirection().multiply(dist));
    }

    public static Location getAbsoluteLocationToRight(Location loc, double dist){
        Location clone = loc.clone();
        clone.setPitch(0);
        return getLocationToRight(clone, dist);
    }

    public static Location getLocationToLeft(Location loc, double dist){
        Location leftDirectionLocation = loc.clone();
        // shifts direction to the left
        leftDirectionLocation.setYaw(leftDirectionLocation.getYaw() - 90);
        return loc.clone().add(leftDirectionLocation.getDirection().multiply(dist));
    }

    public static Location getAbsoluteLocationToLeft(Location location, double numBlocksToLeft){
        Location clone = location.clone();
        clone.setPitch(0);
        return getLocationToLeft(clone, numBlocksToLeft);
    }

    public static Location getLocationInDirection(Location start, Location end, double blocksForward){
        Location startClone = start.clone();
        startClone = startClone.setDirection(end.toVector().subtract(startClone.toVector()));
        Location startClone2 = start.clone();
        startClone2.setYaw(startClone.getYaw());
        return start.clone().add(startClone2.getDirection().multiply(blocksForward));
    }

    public static boolean hasMoved(Location to, Location from, boolean checkY){
        if(to != null){
            boolean hasMovedHorizontally = from.getX() != to.getX() || from.getZ() != to.getZ();
            if(!hasMovedHorizontally && checkY){
                return from.getY() != to.getY();
            }
            return hasMovedHorizontally;
        }
        return false;
    }

    public static Location getMiddleOfBlock(Location blockLocation) {
        return new Location(blockLocation.getWorld(),
                Location.locToBlock(blockLocation.getX()) + 0.5,
                Location.locToBlock(blockLocation.getY()),
                Location.locToBlock(blockLocation.getZ()) + 0.5);
    }

}
