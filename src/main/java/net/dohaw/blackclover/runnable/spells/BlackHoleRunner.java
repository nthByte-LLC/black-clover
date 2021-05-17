package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.type.dark.BlackHole;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ThreadLocalRandom;

public class BlackHoleRunner extends BukkitRunnable {

    private int maxCoordinateAdditive;
    private double reach;
    private double forceMultiplier;

    private BukkitTask teleporter;
    private BukkitTask blackHoleExtras;
    private Location blackHoleLocation;

    private Player caster;

    public BlackHoleRunner(Player caster, BlackHole spell, Location blackHoleLocation){
        this.caster = caster;
        this.maxCoordinateAdditive = spell.getMaxCoordinateAdditive();
        this.reach = spell.getReach();
        this.forceMultiplier = spell.getForceMultiplier();
        this.blackHoleLocation = new Location(blackHoleLocation.getWorld(),
                Location.locToBlock(blackHoleLocation.getX()) + 1,
                Location.locToBlock(blackHoleLocation.getY()),
                Location.locToBlock(blackHoleLocation.getZ()) + 1);
        startTeleporter();
        startBlackHoleExtras();
    }

    @Override
    public void run() {
        for(Entity entity : blackHoleLocation.getWorld().getNearbyEntities(blackHoleLocation, reach, reach, reach)){
            if(!entity.getUniqueId().equals(caster.getUniqueId())){
                entity.setVelocity(blackHoleLocation.toVector().subtract(entity.getLocation().toVector()).normalize().multiply(forceMultiplier));
                SpellUtils.playSound(blackHoleLocation, Sound.BLOCK_BONE_BLOCK_STEP);
            }
        }
    }

    /**
     * Starts the runnable that randomly teleports players when they enter the black hole
     */
    private void startTeleporter(){

        ThreadLocalRandom current = ThreadLocalRandom.current();
        this.teleporter = new BukkitRunnable() {
            @Override
            public void run() {
                for(Entity entity : blackHoleLocation.getWorld().getNearbyEntities(blackHoleLocation, 1, 2, 1)){

                    if(!entity.getUniqueId().equals(caster.getUniqueId())){

                        int deathChance = current.nextInt(100);
                        // 5% chance they get teleported to their death
                        if(deathChance < 5){
                            entity.teleport(new Location(entity.getWorld(), 0, -5, 0));
                            continue;
                        }

                        Location currentEntityLocation = entity.getLocation();
                        int newX = currentEntityLocation.getBlockX() + current.nextInt(-maxCoordinateAdditive, maxCoordinateAdditive);
                        int newZ = currentEntityLocation.getBlockZ() + current.nextInt(-maxCoordinateAdditive, maxCoordinateAdditive);
                        int newY = entity.getWorld().getHighestBlockYAt(newX, newZ);
                        Location newLocation = new Location(entity.getWorld(), newX, newY, newZ);

                        entity.teleport(newLocation);

                    }

                }
            }
        }.runTaskTimer(Grimmoire.instance, 0L, 5L);

    }

    /**
     * Starts the runnable that plays the sounds and spawns the particles in for the black hole periodically.
     */
    private void startBlackHoleExtras(){
        this.blackHoleExtras = new BukkitRunnable(){
            @Override
            public void run() {
                SpellUtils.playSound(blackHoleLocation, Sound.ITEM_CHORUS_FRUIT_TELEPORT);
                SpellUtils.spawnParticle(blackHoleLocation, Particle.SQUID_INK, 30, 0.5f, 0.5f,0.5f);
            }
        }.runTaskTimer(Grimmoire.instance, 0L, 20L);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        teleporter.cancel();
        blackHoleExtras.cancel();
    }

}
