package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FireBlastRunner extends BukkitRunnable {

    private int distanceNeeded;
    private Player caster;

    private int distanceCovered = 1;

    private List<Location> beamLocations = new ArrayList<>();
    private BukkitTask particleSpawner;
    private BukkitTask damager;

    private final int BASE_DAMAGE = 3;
    private double damageScale;

    public FireBlastRunner(Player caster, int distanceNeeded, double damageScale){
        this.caster = caster;
        this.distanceNeeded = distanceNeeded;
        this.damageScale = damageScale;
        initRunners();
    }

    @Override
    public void run() {

        Location particleLocation = caster.getLocation().add(caster.getLocation().getDirection().multiply(distanceCovered));
        beamLocations.add(particleLocation);

        distanceCovered++;

        if(distanceCovered == distanceNeeded){
            cancel();
            particleSpawner.cancel();
            damager.cancel();
            SpellUtils.spawnParticle(particleLocation, Particle.EXPLOSION_LARGE, 30, 1, 1, 1);
            for(Entity e : particleLocation.getWorld().getNearbyEntities(particleLocation, 3, 3, 3)){
                if(e instanceof LivingEntity){
                    LivingEntity le = (LivingEntity) e;
                    double damageDone = (BASE_DAMAGE * damageScale) + SpellUtils.getRandomDamageModifier();
                    SpellDamageEvent event = new SpellDamageEvent(SpellType.FIRE_BLAST, damageDone, le, caster);
                    Bukkit.getPluginManager().callEvent(event);
                    if(!event.isCancelled()){
                        le.damage(event.getDamage(), caster);
                    }
                }
            }
        }

    }

    private void initRunners(){
        this.particleSpawner = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            for(Location loc : beamLocations){
                SpellUtils.spawnParticle(loc, Particle.FLAME, 5, 0, 0, 0);
            }
        }, 0L, 1L);
        this.damager = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            for(Location loc : beamLocations){
                Collection<Entity> entitiesInBeam = loc.getWorld().getNearbyEntities(loc, 1, 1, 1);
                for(Entity e : entitiesInBeam){
                    if(e instanceof LivingEntity){
                        LivingEntity le = (LivingEntity) e;
                        double damageDone = (BASE_DAMAGE * damageScale) + SpellUtils.getRandomDamageModifier();
                        SpellDamageEvent event = new SpellDamageEvent(SpellType.FIRE_BLAST, damageDone, le, caster);
                        Bukkit.getPluginManager().callEvent(event);
                        if(!event.isCancelled()){
                            le.damage(event.getDamage(), caster);
                        }
                    }
                }
            }
        }, 0, 3L);
    }

}
