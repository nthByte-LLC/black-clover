package net.dohaw.blackclover.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BlockUtil {

    public static Block getBlockToSide(Location loc){
        final int newZ = (int) (loc.getZ() + ( 1 * Math.sin(Math.toRadians(loc.getYaw()))));
        final int newX = (int) (loc.getX() + ( 1 * Math.cos(Math.toRadians(loc.getYaw()))));
        return loc.getWorld().getBlockAt(newX, loc.getBlockY(), newZ);
    }

    public static Location getBlockInFront(Entity entity, int numBlocksInFront){
        Location entityLocation = entity.getLocation();
        return entityLocation.add(entityLocation.getDirection().multiply(numBlocksInFront));
    }

}
