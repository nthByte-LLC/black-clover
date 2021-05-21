package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.sand.Earthquake;
import net.dohaw.blackclover.util.BlockSnapshot;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EarthquakeRunner extends BukkitRunnable {

    private final double MAX_BLOCK_Y_ADDITIVE = 1;

    private List<BlockSnapshot> waveBlocksRemoved = new ArrayList<>();

    private Location originEarthquake;

    private int radius, radiusCount;
    private int numberOfWaves, waveCount;

    private Player caster;

    private double damage;

    public EarthquakeRunner(Player caster, Location originEarthquake, Earthquake earthquake){
        this.caster = caster;
        this.originEarthquake = originEarthquake;
        this.radius =  earthquake.getRadius();
        this.numberOfWaves = earthquake.getNumberOfWaves();
        this.damage = earthquake.getDamage();
    }

    @Override
    public void run() {

        double earthquakeStartX = originEarthquake.getX();
        double earthquakeStartY = originEarthquake.getY();
        double earthquakeStartZ = originEarthquake.getZ();
        World earthquakeWorld = originEarthquake.getWorld();

        for(double x = earthquakeStartX - radiusCount; x <= earthquakeStartX + radiusCount; x++){
            for(double z = earthquakeStartZ - radiusCount; z <= earthquakeStartZ + radiusCount; z++){
                Location loc = new Location(earthquakeWorld, x, earthquakeStartY, z);
                Block block = loc.getBlock();
                if(block.getType().isSolid()){
                    double minX = earthquakeStartX - radiusCount;
                    double maxX = earthquakeStartX + radiusCount;
                    double minZ = earthquakeStartZ - radiusCount;
                    double maxZ = earthquakeStartZ + radiusCount;
                    if(x == minX || x == maxX || z == minZ || z == maxZ){
                        spawnFallingBlock(block);
                    }
                }
            }
        }

        radiusCount++;

        if(radiusCount == radius){
            doEarthquakeDamage();
            radiusCount = 0;
            waveCount++;
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, this::finishWave, 1);
        }

        if(waveCount == numberOfWaves){
            cancel();
        }

    }

    private void doEarthquakeDamage(){
        for(Entity entity : originEarthquake.getWorld().getNearbyEntities(originEarthquake, radiusCount, 2, radiusCount)){
            if(!entity.getUniqueId().equals(caster.getUniqueId()) && !(entity instanceof FallingBlock)){
                // Little bit of knockback upwards
                Vector velocity = entity.getVelocity();
                velocity.setY(0.33333);
                entity.setVelocity(velocity);
                if(entity instanceof LivingEntity){
                    LivingEntity livingEntity = (LivingEntity) entity;
                    SpellUtils.doSpellDamage(livingEntity, caster, SpellType.EARTHQUAKE, damage);
                }
            }
        }
    }

    private void spawnFallingBlock(Block blockBeingRemoved){
        BlockSnapshot snapshot = BlockSnapshot.toSnapshot(blockBeingRemoved);
        // some blocks get added twice so this is important to check
        if(!waveBlocksRemoved.contains(snapshot)){
            waveBlocksRemoved.add(snapshot);
            blockBeingRemoved.setType(Material.AIR);
            double randomYAdditive = ThreadLocalRandom.current().nextDouble(0.1, MAX_BLOCK_Y_ADDITIVE);
            Location fallingBlockLoc = blockBeingRemoved.getLocation().clone().add(0, randomYAdditive, 0);
            FallingBlock fallingBlock = fallingBlockLoc.getWorld().spawnFallingBlock(fallingBlockLoc, snapshot.getData().getMaterial().createBlockData());
            fallingBlock.setHurtEntities(true);
            fallingBlock.setDropItem(false);
        }
    }

    private void finishWave(){

       for(BlockSnapshot snapshot : waveBlocksRemoved){
           snapshot.apply();
       }
       waveBlocksRemoved.clear();

    }

}
