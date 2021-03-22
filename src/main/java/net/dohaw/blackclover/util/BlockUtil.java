package net.dohaw.blackclover.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class BlockUtil {

    public static Block getBlockToSide(Location loc){
        final int newZ = (int) (loc.getZ() + ( 1 * Math.sin(Math.toRadians(loc.getYaw()))));
        final int newX = (int) (loc.getX() + ( 1 * Math.cos(Math.toRadians(loc.getYaw()))));
        return loc.getWorld().getBlockAt(newX, loc.getBlockY(), newZ);
    }

    public static Location getLocationInFront(Entity entity, int numBlocksInFront){
        return getLocationInFront(entity.getLocation(), numBlocksInFront);
    }

    public static Location getLocationInFront(Location location, int numBlocksInFront){
        return location.add(location.getDirection().multiply(numBlocksInFront));
    }

    public static Location getRightBlock(Location loc, int numBlocks){
        Location rightDirectionLocation = loc.clone();
        // shifts direction to the right
        rightDirectionLocation.setYaw(rightDirectionLocation.getYaw() + 90);
        return loc.add(rightDirectionLocation.getDirection().multiply(numBlocks));
    }

    public static Location getLeftBlock(Location loc, int numBlocks){
        Location leftDirectionLocation = loc.clone();
        // shifts direction to the left
        leftDirectionLocation.setYaw(leftDirectionLocation.getYaw() - 90);
        return loc.add(leftDirectionLocation.getDirection().multiply(numBlocks));
    }

}
