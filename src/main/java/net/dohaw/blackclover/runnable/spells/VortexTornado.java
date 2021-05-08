package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.runnable.particle.EntityRunner;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class VortexTornado extends BukkitRunnable {

    // Keeps track of all the invisible armor stands we have placed and removes them from the world later.
    private List<Entity> invisibleArmorStands = new ArrayList<>();

    // The maximum distance the tornado should travel before this is canceled.
    private final double MAX_DISTANCE_TRAVEL;

    private double distanceTraveled = 0;

    private int numStayedIntervals = 0;

    private Entity entity;
    private final Particle.DustOptions DUST_OPTIONS;
    private List<TornadoParticleRunner> tornadoes = new ArrayList<>();

    public VortexTornado(Entity entity, Particle.DustOptions dustOptions, int maxDistanceTravel) {
        this.entity = entity;
        this.DUST_OPTIONS = dustOptions;
        this.MAX_DISTANCE_TRAVEL = maxDistanceTravel;
        initTornadoes();
    }

    @Override
    public void run() {

        invisibleArmorStands.add(entity);
        // Moves the tornado forward if the block in front is not a solid
        Location locInFront = LocationUtil.getLocationInFront(entity, 0.5);
        if(!locInFront.getBlock().getType().isSolid()){
            System.out.println("Entity Location: " + entity.getLocation());
            entity = SpellUtils.invisibleArmorStand(locInFront);
            // Changes the entity for all the child entities
            for(TornadoParticleRunner childTornado : tornadoes){
                childTornado.setEntity(entity);
            }
            distanceTraveled += 0.5;
        }else{
            numStayedIntervals++;
        }

        /*
            The tornado will only move forward if it is allowed to move forward. It'll only stay in place for so long until it stops.
         */
        final int ALLOWED_STAYED_INTERVALS = 100;
        if(numStayedIntervals == ALLOWED_STAYED_INTERVALS || distanceTraveled == MAX_DISTANCE_TRAVEL){
            cancel();
        }

    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        for(TornadoParticleRunner childTornado: tornadoes){
            childTornado.cancel();
        }
        for(Entity as : invisibleArmorStands){
            as.remove();
        }
    }

    private void initTornadoes(){

        final int NUM_OUTER_TORNADOES = 1;
        for(int i = 0; i < NUM_OUTER_TORNADOES; i++){
            TornadoParticleRunner currentTornado = new TornadoParticleRunner(entity, DUST_OPTIONS, i + 1, false);
            TornadoParticleRunner currentTornado2 = new TornadoParticleRunner(entity, DUST_OPTIONS, i + 1, true);
            currentTornado.setMaxYAdditive(50);
            currentTornado2.setMaxYAdditive(50);

            currentTornado.runTaskTimer(Grimmoire.instance, 0L, 1L);
            currentTornado2.runTaskTimer(Grimmoire.instance, 0L, 1L);
            tornadoes.add(currentTornado);
            tornadoes.add(currentTornado2);
        }

    }

}
