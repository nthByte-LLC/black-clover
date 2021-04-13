package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AshBeamRunner extends BeamDamager {

    private double damage;

    public AshBeamRunner(Entity start, double distanceBeam, double damage) {
        super(start, distanceBeam, new Particle.DustOptions(Color.BLACK, 1f), false);
        this.damage = damage;
    }

    @Override
    public void doEndOfBeamSpecifics() {
        if(particleLocations.size() != 0){
            Location lastLocation = particleLocations.get(particleLocations.size() - 1);
            SpellUtils.spawnParticle(lastLocation,Particle.FLASH, 10, 1, 1, 1);
            SpellUtils.playSound(lastLocation, Sound.ENTITY_GENERIC_EXPLODE);
        }
    }

    @Override
    public boolean firstBeamEntranceActions(LivingEntity livingEntity) {

        SpellDamageEvent event = new SpellDamageEvent(SpellType.ASH_BEAM, damage, livingEntity, (Player) entity);
        Bukkit.getPluginManager().callEvent(event);

        boolean isEventCancelled = event.isCancelled();
        if(!event.isCancelled()){
            livingEntity.damage(damage);
        }

        return !isEventCancelled;

    }

    @Override
    public void doDamagerSpecifics(LivingEntity entityInBeam) {

        SpellDamageEvent event = new SpellDamageEvent(SpellType.ASH_BEAM, damage, entityInBeam, (Player) entity);
        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled()){
            entityInBeam.damage(damage);
            entityInBeam.playEffect(EntityEffect.HURT);
            SpellUtils.spawnParticle(entityInBeam, Particle.HEART, 20, 0.3f, 0.3f, 0.3f);
            SpellUtils.playSound(entityInBeam, Sound.BLOCK_BONE_BLOCK_BREAK);
        }


    }

}
