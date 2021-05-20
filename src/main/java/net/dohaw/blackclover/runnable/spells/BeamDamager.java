package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.runnable.particle.BeamParticleRunner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public abstract class BeamDamager extends BeamParticleRunner {

    private BukkitTask damageDealer;
    private HashSet<UUID> inBeam = new HashSet<>();

    public BeamDamager(LivingEntity start, double distanceBeam, Particle.DustOptions dustOptions, boolean isPersistent) {
        super(start, distanceBeam, dustOptions, 0.2, isPersistent);
        startDamager();
    }

    /**
     * What is done when the player first enters the beam
     * @return Whether it should proceed to add the player to the list of players that have been hit by the beam
     */
    public abstract boolean firstBeamEntranceActions(LivingEntity livingEntity);

    /**
     * What is done inside of the damager.
     */
    public abstract void doDamagerSpecifics(LivingEntity entityInBeam);

    @Override
    public void doParticleLocationSpecifics(Location particleLoc) {

        // People in current beam
        Collection<Entity> nearbyEntities = particleLoc.getWorld().getNearbyEntities(particleLoc, 0.5, 0.5, 0.5);
        // so that we don't target the caster.
        nearbyEntities.removeIf(e -> e.getUniqueId().equals(entity.getUniqueId()));
        List<UUID> inCurrentBeam = new ArrayList<>();
        for(Entity e : nearbyEntities){
            if(e instanceof LivingEntity){

                LivingEntity le = (LivingEntity) e;
                inCurrentBeam.add(le.getUniqueId());
                UUID uuid = le.getUniqueId();
                if(!inBeam.contains(uuid)){
                    boolean shouldAddToBeamList = firstBeamEntranceActions(le);
                    if (shouldAddToBeamList) {
                        inBeam.add(uuid);
                    }
                }

            }
        }

        /*
            If the player isn't in the current beam, but was in last iteration's beam then they aren't in the beam anymore and they shouldn't be taking damage
         */
        inBeam.removeIf(uuid -> !inCurrentBeam.contains(uuid));

    }

    /*
        Damages the player every second if they are in the beam
     */
    private void startDamager(){

        this.damageDealer = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {

            Iterator<UUID> it = inBeam.iterator();
            while(it.hasNext()){
                UUID uuid = it.next();
                LivingEntity le = Bukkit.getPlayer(uuid);
                if(le != null){
                    if(!le.isDead()){
                        doDamagerSpecifics(le);
                    }else{
                        it.remove();
                    }
                }else{
                    it.remove();
                }
            }

        },0L, 20L);

    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        System.out.println("STOPPING DAMAGE DEALER");
        this.damageDealer.cancel();
        super.cancel();
    }

}
