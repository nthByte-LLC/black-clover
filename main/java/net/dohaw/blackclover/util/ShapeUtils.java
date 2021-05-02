package net.dohaw.blackclover.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;

import java.util.ArrayList;
import java.util.List;

public class ShapeUtils {

    /**
     *
     * @param centerBlock Define the center of the sphere
     * @param radius Radius of your sphere
     * @param hollow If your sphere should be hollow (you only get the blocks outside) just put in "true" here
     * @return Returns the locations of the blocks in the sphere
     *
     */
    public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {

        List<Location> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {

                    double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));

                    if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location l = new Location(centerBlock.getWorld(), x, y, z);

                        circleBlocks.add(l);

                    }

                }
            }
        }

        return circleBlocks;
    }

    public static List<Block> getBlocksInCube(Location start, int radius){
        List<Block> blocks = new ArrayList<>();
        for(double x = start.getX() - radius; x <= start.getX() + radius; x++){
            for(double y = start.getY() - radius; y <= start.getY() + radius; y++){
                for(double z = start.getZ() - radius; z <= start.getZ() + radius; z++){
                    Location loc = new Location(start.getWorld(), x, y, z);
                    blocks.add(loc.getBlock());
                }
            }
        }
        return blocks;
    }

    public static List<FallingBlock> createFallingBlockCube(Location start, Material fallingBlockMaterial, int xDimension, int yDimension, int zDimension){
        List<FallingBlock> fallingBlocks = new ArrayList<>();
        for(double x = start.getX(); x < start.getX() + xDimension; x++){
            for(double y = start.getY(); y < start.getY() + yDimension; y++){
                for(double z = start.getZ(); z < start.getZ() + zDimension; z++){
                    Location loc = new Location(start.getWorld(), x, y, z);
                    fallingBlocks.add(start.getWorld().spawnFallingBlock(loc, fallingBlockMaterial.createBlockData()));
                }
            }
        }
        System.out.println("FALLING BLOCKS SIZE: " + fallingBlocks.size());
        return fallingBlocks;
    }

    public static List<Block> getHollowCube(Location start, int radius){
        List<Block> blocks = new ArrayList<>();
        for(double x = start.getBlockX() - radius; x <= start.getBlockX() + radius; x++){
            for(double y = start.getBlockY() - radius; y <= start.getBlockY() + radius; y++){
                for(double z = start.getBlockZ() - radius; z <= start.getBlockZ() + radius; z++){
                    if(x == ((start.getBlockX() + radius) - 1) || y == ((start.getBlockY() + radius) - 1) || z == ((start.getBlockZ() + radius) - 1) || x == (start.getBlockX() - radius) || y == (start.getBlockY() - radius) || z == (start.getBlockZ() - radius)){
                        Location loc = new Location(start.getWorld(), x, y, z);
                        blocks.add(loc.getBlock());
                    }
                }
            }
        }
        return blocks;
    }

    public static List<BlockSnapshot> getBlockSnapshotsInCube(Location start, int radius){
        List<BlockSnapshot> blocks = new ArrayList<>();
        for(double x = start.getX() - radius; x <= start.getX() + radius; x++){
            for(double y = start.getY() - radius; y <= start.getY() + radius; y++){
                for(double z = start.getZ() - radius; z <= start.getZ() + radius; z++){
                    Location loc = new Location(start.getWorld(), x, y, z);
                    Block block = loc.getBlock();
                    blocks.add(new BlockSnapshot(block.getBlockData(), loc));
                }
            }
        }
        return blocks;
    }

}
