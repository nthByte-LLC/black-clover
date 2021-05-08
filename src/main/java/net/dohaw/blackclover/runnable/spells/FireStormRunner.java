package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.type.fire.FireStorm;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class FireStormRunner extends BukkitRunnable {

    private Player player;
    private int numFireBalls, numWaves, numIterationsRan = 0;
    private double radius;

    private BukkitTask particleRunner1, particleRunner2;

    private FireStorm fireStorm;

    public FireStormRunner(Player player, FireStorm fireStorm){
        this.player = player;
        this.fireStorm = fireStorm;
        this.numFireBalls = fireStorm.getNumFireBalls();
        this.numWaves = fireStorm.getNumFireballWaves();
        this.radius = fireStorm.getRadiusParticles();
        initRingOfFire();
    }

    @Override
    public void run() {

        numIterationsRan++;

        World world = player.getWorld();
        Location origin = player.getLocation();
        double yawAdditive = 360.0 / numFireBalls;
        float yaw = 0;

        for (int i = 0; i < numFireBalls; i++) {

            Location fireBallLoc = origin.clone();
            fireBallLoc.setYaw(yaw);
            fireBallLoc.setPitch(0);

            Fireball entitySpawned = (Fireball) world.spawnEntity(fireBallLoc, EntityType.SMALL_FIREBALL);
            fireStorm.markAsSpellBinding(entitySpawned);

            entitySpawned.setIsIncendiary(false);
            entitySpawned.setGravity(false);
            entitySpawned.setYield(0);
            entitySpawned.setFireTicks(0);

            yaw += yawAdditive;

        }

        if(numIterationsRan == numWaves){
            cancel();
            particleRunner1.cancel();
            particleRunner2.cancel();
        }

    }

    private void initRingOfFire(){

        TornadoParticleRunner runner1 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.RED, 1),  radius, true);
        TornadoParticleRunner runner2 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.ORANGE, 1), radius, false);

        this.particleRunner1 = runner1.runTaskTimer(Grimmoire.instance, 0L, 1L);
        this.particleRunner2 = runner2.runTaskTimer(Grimmoire.instance, 0L, 1L);

    }

}
