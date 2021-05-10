package net.dohaw.blackclover.runnable.spells.vortex;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public abstract class AbstractVortexTornado extends BukkitRunnable {

    // Keeps track of all the invisible armor stands we have placed and removes them from the world later.
    private List<Entity> invisibleArmorStands = new ArrayList<>();

    // The maximum distance the tornado should travel before this is canceled.
    private final int MAX_DISTANCE_TRAVEL;

    private double distanceTraveled = 0;

    private int numStayedIntervals = 0;

    protected Entity entity;
    private final Particle.DustOptions DUST_OPTIONS;
    protected List<TornadoParticleRunner> tornadoes = new ArrayList<>();

    // The task that carries out the specifics of what the tornado is supposed to do
    private BukkitTask innerTask;

    public AbstractVortexTornado(Entity entity, Particle.DustOptions dustOptions, int maxDistanceTravel, long intervalInnerRunner) {
        this.entity = entity;
        this.DUST_OPTIONS = dustOptions;
        this.MAX_DISTANCE_TRAVEL = maxDistanceTravel;
        initTornadoes();
        this.innerTask = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, this::doTornadoSpecifics, 0L, intervalInnerRunner);
    }

    @Override
    public void run() {

        invisibleArmorStands.add(entity);
        // Moves the tornado forward if the block in front is not a solid
        Location locInFront = LocationUtil.getLocationInFront(entity, 0.5);
        if(!locInFront.getBlock().getType().isSolid()){
            entity = SpellUtils.invisibleArmorStand(locInFront);
            // Changes the entity for all the child entities
            for(TornadoParticleRunner childTornado : tornadoes){
                childTornado.setEntity(entity);
                // Shifts the tornado particles forward to make it look like the tornado is moving forward as well.
                ListIterator<Location> itr = childTornado.getParticleLocations().listIterator();
                while(itr.hasNext()){
                    Location currentParticleLocation = itr.next();
                    itr.set(LocationUtil.getLocationInFront(currentParticleLocation, 0.5));
                }
            }
            distanceTraveled += 0.5;
        }else{
            numStayedIntervals++;
        }

        /*
            The tornado will only move forward if it is allowed to move forward. It'll only stay in place for so long until it stops.
         */
        final int ALLOWED_STAYED_INTERVALS = 50;
        if(numStayedIntervals >= ALLOWED_STAYED_INTERVALS || distanceTraveled >= MAX_DISTANCE_TRAVEL){
            System.out.println("HERE");
            cancel();
        }

    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        innerTask.cancel();
        for(TornadoParticleRunner childTornado: tornadoes){
            childTornado.cancel();
        }
        for(Entity as : invisibleArmorStands){
            as.remove();
        }
    }

    private void initTornadoes(){

        TornadoParticleRunner currentTornado = new TornadoParticleRunner(entity, DUST_OPTIONS, 1, false);
        TornadoParticleRunner currentTornado2 = new TornadoParticleRunner(entity, DUST_OPTIONS, 1, true);
        currentTornado.setMaxYAdditive(50);
        currentTornado2.setMaxYAdditive(50);

        currentTornado.runTaskTimer(Grimmoire.instance, 0L, 1L);
        currentTornado2.runTaskTimer(Grimmoire.instance, 0L, 1L);
        tornadoes.add(currentTornado);
        tornadoes.add(currentTornado2);

    }

    public abstract void doTornadoSpecifics();

    protected Collection<Entity> getEntitiesNearTornado(){
        TornadoParticleRunner firstTornado = tornadoes.get(0);
        // Gets the entities as high as the tornado and 2 blocks in each x and y direction.
        return entity.getNearbyEntities(2, firstTornado.yIncrease, 2);
    }

}
