package net.dohaw.blackclover.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class LocationUtil {

    public static Block getBlockToSide(Location loc){
        final int newZ = (int) (loc.getZ() + ( 1 * Math.sin(Math.toRadians(loc.getYaw()))));
        final int newX = (int) (loc.getX() + ( 1 * Math.cos(Math.toRadians(loc.getYaw()))));
        return loc.getWorld().getBlockAt(newX, loc.getBlockY(), newZ);
    }

    public static Location getLocationInFront(Entity entity, double numBlocksInFront){
        return getLocationInFront(entity.getLocation(), numBlocksInFront);
    }

    public static Location getLocationInFront(Location location, double numBlocksInFront){
        return location.add(location.getDirection().multiply(numBlocksInFront));
    }

    public static Location getLocationToRight(Location loc, double dist){
        Location rightDirectionLocation = loc.clone();
        // shifts direction to the right
        rightDirectionLocation.setYaw(rightDirectionLocation.getYaw() + 90);
        return loc.clone().add(rightDirectionLocation.getDirection().multiply(dist));
    }

    public static Location getLocationToLeft(Location loc, double dist){
        Location leftDirectionLocation = loc.clone();
        // shifts direction to the left
        leftDirectionLocation.setYaw(leftDirectionLocation.getYaw() - 90);
        return loc.clone().add(leftDirectionLocation.getDirection().multiply(dist));
    }

    public static boolean hasMoved(Location to, Location from){
        if(to != null){
            return from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ();
        }
        return false;
    }

}
