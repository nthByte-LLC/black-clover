package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.runnable.particle.BeamParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class AshBeamRunner extends BeamParticleRunner {

    private BukkitTask damageDealer;
    private double damage;
    private HashSet<UUID> inBeam = new HashSet<>();

    public AshBeamRunner(Entity start, double distanceBeam, double damage) {
        super(start, distanceBeam, new Particle.DustOptions(Color.BLACK, 1.5f), 0.5, false);
        this.damage = damage;
        startDamager();
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    public void doEndOfBeamSpecifics() {
        System.out.println("WE ARE AT THE END OF THE BEAM");
    }

    @Override
    public void doParticleLocationSpecifics(Location particleLoc) {

        // People in current beam
        Collection<Entity> nearbyEntities = particleLoc.getWorld().getNearbyEntities(particleLoc, 1, 1, 1);
        // so that we don't target the caster.
        nearbyEntities.removeIf(e -> e.getUniqueId().equals(entity.getUniqueId()));
        List<UUID> inCurrentBeam = new ArrayList<>();
        for(Entity e : nearbyEntities){
            if(e instanceof LivingEntity){

                LivingEntity le = (LivingEntity) e;
                inCurrentBeam.add(le.getUniqueId());
                UUID uuid = le.getUniqueId();
                if(!inBeam.contains(uuid)){

                    SpellDamageEvent event = new SpellDamageEvent(SpellType.ASH_BEAM, damage, le, (Player) start);
                    Bukkit.getPluginManager().callEvent(event);

                    if(!event.isCancelled()){
                        // damage them and add them to the list of player's that are in the beam
                        le.damage(damage);
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
                        le.damage(damage);
                        le.playEffect(EntityEffect.HURT);
                        SpellUtils.spawnParticle(le, Particle.HEART, 20, 0.3f, 0.3f, 0.3f);
                        SpellUtils.playSound(le, Sound.BLOCK_BONE_BLOCK_BREAK);
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
        super.cancel();
        this.damageDealer.cancel();
    }
}
