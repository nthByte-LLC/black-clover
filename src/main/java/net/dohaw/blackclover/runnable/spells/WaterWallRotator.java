package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.util.BlockUtil;
import net.dohaw.corelib.JPUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WaterWallRotator extends BukkitRunnable implements Listener {

    private List<Location> waterWallBlocks = new ArrayList<>();
    private Player caster;

    private final int WALL_HEIGHT = 5;
    // only odd number wall widths work right now. I think i'm going to have to re-write all this to get even numbers to work. This is fine for now...
    private final int WALL_WIDTH = 3;

    public WaterWallRotator(Player caster){
        this.caster = caster;
        JPUtils.registerEvents(this);
    }

    @Override
    public void run() {

        waterWallBlocks.forEach(loc -> {
            loc.getBlock().setType(Material.AIR);
            unmarkBlock(loc.getBlock());
        });
        waterWallBlocks.clear();

        // Gets the first block placed.
        Location locationInFront = BlockUtil.getLocationInFront(caster, 2);
        if(locationInFront.getBlock().getType() == Material.AIR){
            waterWallBlocks.add(locationInFront);
            markBlock(locationInFront.getBlock());
        }

        // not going to lie, i don't entirely know what this does for my code but it works.
        int wallWidth = WALL_WIDTH % 3 == 0 ? (WALL_WIDTH / 3) : (WALL_WIDTH / 3) + 1;

        for (int x = 0; x <= wallWidth; x++) {

            // This loop makes the x iteration run twice on the same iteration.
            /*
                How we want this to work is fill in the columns to the left and right of the initial block (locationInFront variable) in one X iteration.
             */
            for(int z = 1; z <= 2; z++){
                Location firstColumnBlock = locationInFront.clone();
                if(z % 2 == 0){
                    firstColumnBlock = BlockUtil.getLocationToRight(firstColumnBlock, x);
                }else{
                    firstColumnBlock = BlockUtil.getLocationToLeft(firstColumnBlock, x);
                }

                // Fills in the columns.
                for (int i = 1; i < WALL_HEIGHT; i++) {
                    Location nextLoc = firstColumnBlock.clone().add(0, i, 0);
                    if(nextLoc.getBlock().getType() == Material.AIR){
                        waterWallBlocks.add(nextLoc);
                    }
                }

            }

        }

        waterWallBlocks.forEach(loc -> {
            markBlock(loc.getBlock());
            loc.getBlock().setType(Material.WATER);
        });

    }

    private void markBlock(Block b){
        b.setMetadata("water-wall-mark", new FixedMetadataValue(Grimmoire.instance, true));
    }

    private void unmarkBlock(Block b){
        b.removeMetadata("water-wall-mark", Grimmoire.instance);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        waterWallBlocks.forEach(loc -> {
            unmarkBlock(loc.getBlock());
            loc.getBlock().setType(Material.AIR);
        });
    }

    @EventHandler
    public void onWaterBlockMove(BlockFromToEvent e){
        if(e.getBlock().hasMetadata("water-wall-mark") || e.getBlock().hasMetadata("water-wall-mark")){
            e.setCancelled(true);
        }
    }

}
