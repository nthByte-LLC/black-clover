package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.util.BlockUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WaterWallRotator extends BukkitRunnable {

    private BlockFace previousFacing = null;
    private List<Location> waterWallBlocks = new ArrayList<>();
    private Player caster;

    private final int WALL_HEIGHT = 5;
    private final int WALL_WIDTH = 3;

    public WaterWallRotator(Player caster){
        this.caster = caster;
    }

    @Override
    public void run() {


        // does this so that we don't waste resources placing the same water wall.
        BlockFace facing = caster.getFacing();
        if(previousFacing != null){
            if(facing == previousFacing){
                return;
            }else{
                previousFacing = facing;
                waterWallBlocks.forEach(loc -> loc.getBlock().setType(Material.AIR));
                waterWallBlocks.clear();
            }
        }else{
            previousFacing = facing;
        }

        int currentWallHeight = 0;

        Location locationInFront = BlockUtil.getBlockInFront(caster, 2);
        if(locationInFront.getBlock().getType() == Material.AIR){
            waterWallBlocks.add(locationInFront);
        }
        currentWallHeight++;

        for (int x = 0; x < WALL_WIDTH; x++) {

            for(int z = 0; z < 2; z++){
                Location firstColumnBlock = locationInFront.clone();
                if(x != 0){
                    if(x % 2 == 0){
                        firstColumnBlock = BlockUtil.getRightBlock(firstColumnBlock, x);
                    }else{
                        firstColumnBlock = BlockUtil.getLeftBlock(firstColumnBlock, x);
                    }
                }

                for (int i = currentWallHeight; i < WALL_HEIGHT; i++) {
                    Location nextLoc = firstColumnBlock.clone().add(0, i, 0);
                    if(nextLoc.getBlock().getType() == Material.AIR){
                        waterWallBlocks.add(nextLoc);
                        System.out.println("HERE");
                    }
                }
            }


        }
        System.out.println("WATER BLOCKS SIZE: " + waterWallBlocks.size());

        waterWallBlocks.forEach(loc -> loc.getBlock().setType(Material.EMERALD_BLOCK));

    }

}
