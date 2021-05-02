package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.type.sand.Earthquake;
import net.dohaw.blackclover.util.BlockSnapshot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EarthquakeRunner extends BukkitRunnable {

    private int radius;
    private int originX, originY, originZ;
    private Player player;
    private int currentRadius = 1;
    private int currentWave = 0;
    private final int WAVES = 3;

    private final double MAX_BLOCK_Y_ADDITIVE = 0.5;

    private Queue<BlockSnapshot> blocksRemoved = new LinkedList<>();
    private Queue<FallingBlock> fallingBlocks = new LinkedList<>();

    public EarthquakeRunner(Player player, Earthquake earthquake){
        this.player = player;
        this.originX = player.getLocation().getBlockX();
        this.originY = player.getLocation().getBlockY() - 1;
        this.originZ = player.getLocation().getBlockZ();
        this.radius = earthquake.getRadius();
    }

    @Override
    public void run() {

        // side 1
        Location side1Start = new Location(player.getWorld(), originX - currentRadius, originY, originZ - currentRadius);
        for(int additive = 0; additive < (currentRadius * 2); additive++){
            Location loc = side1Start.clone().add(additive, 0, 0);
            spawnFallingBlock(loc);
        }

        // side 2
        Location side2Start = new Location(player.getWorld(), originX + currentRadius, originY, originZ - currentRadius);
        for(int additive = 0; additive < (currentRadius * 2) + 1; additive++){
            Location loc = side2Start.clone().add(0, 0, additive);
            spawnFallingBlock(loc);
        }

        // side 3
        // added 1 to cover up for missed corner
        Location side3Start = new Location(player.getWorld(), originX - currentRadius, originY, originZ + currentRadius);
        for(int additive = 0; additive < (currentRadius * 2); additive++){
            Location loc = side3Start.clone().add(additive, 0, 0);
            spawnFallingBlock(loc);
        }

        // side 4
        Location side4Start = new Location(player.getWorld(), originX - currentRadius, originY, originZ - currentRadius);
        for(int additive = 0; additive < (currentRadius * 2); additive++){
            Location loc = side4Start.clone().add(0, 0, additive);
            spawnFallingBlock(loc);
        }

        currentRadius++;

        if(radius == currentRadius){
            currentWave++;
            currentRadius = 1;
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, this::finishWave, 1);
        }

        if(currentWave == WAVES){
            this.cancel();
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, this::finishWave, 1);
        }

    }

    private void spawnFallingBlock(Location locationBlockRemoving){
        BlockSnapshot snapshot = BlockSnapshot.toSnapshot(locationBlockRemoving.getBlock());
        // some blocks get added twice so this is important to check
        if(!blocksRemoved.contains(snapshot)){
            blocksRemoved.add(snapshot);
            locationBlockRemoving.getBlock().setType(Material.AIR);
            double randomYAdditive = ThreadLocalRandom.current().nextDouble(0.1, MAX_BLOCK_Y_ADDITIVE);
            Location fallingBlockLoc = locationBlockRemoving.clone().add(0, randomYAdditive, 0);
            FallingBlock fallingBlock = fallingBlockLoc.getWorld().spawnFallingBlock(fallingBlockLoc, snapshot.getData().getMaterial().createBlockData());
            fallingBlock.setHurtEntities(true);
            fallingBlock.setDropItem(false);
            fallingBlocks.add(fallingBlock);
        }
    }

    private void finishWave(){

        FallingBlock fallingBlock;
        while((fallingBlock = fallingBlocks.poll()) != null){
            if(!fallingBlock.isOnGround()){
                fallingBlock.remove();
            }
        }

        BlockSnapshot snapshot;
        while((snapshot = blocksRemoved.poll()) != null){
            BlockData data = snapshot.getData();
            snapshot.getLocation().getBlock().setBlockData(data);
        }

    }

}
