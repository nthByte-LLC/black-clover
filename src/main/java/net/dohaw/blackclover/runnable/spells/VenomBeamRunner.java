package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VenomBeamRunner extends BeamDamager{

    private int poisonLevel;
    private double poisonDuration;

    public VenomBeamRunner(Entity start, double distanceBeam, int poisonLevel, double poisonDuration) {
        super(start, distanceBeam, new Particle.DustOptions(BukkitColor.VIOLET, 1.5f), true);
        this.poisonLevel = poisonLevel;
        this.poisonDuration = poisonDuration;
    }

    @Override
    public boolean firstBeamEntranceActions(LivingEntity livingEntity) {

        SpellDamageEvent event = new SpellDamageEvent(SpellType.VENOM, 0, livingEntity, (Player) entity);
        Bukkit.getPluginManager().callEvent(event);

        boolean isEventCancelled = event.isCancelled();
        if(!isEventCancelled){
            applyPoison(livingEntity);
        }

        return !isEventCancelled;

    }

    /*
        We don't want the locations to update. We just want the beam to constantly be drawn in a certain place
     */
    @Override
    public void updateLocations() { }

    /*
        The longer they stay in the beam, the more intense the poison gets
     */
    @Override
    public void doDamagerSpecifics(LivingEntity entityInBeam) {

        SpellDamageEvent event = new SpellDamageEvent(SpellType.VENOM, 0, entityInBeam, (Player) entity);
        Bukkit.getPluginManager().callEvent(event);

        if(!event.isCancelled()){

            if(entityInBeam.hasPotionEffect(PotionEffectType.POISON)){

                PotionEffect poisonEffect = entityInBeam.getPotionEffect(PotionEffectType.POISON);
                int currentPoisonLevel = poisonEffect.getAmplifier();
                PotionEffect newPoisonEffect = new PotionEffect(PotionEffectType.POISON, (int) (poisonDuration * 20), currentPoisonLevel + 1);

                entityInBeam.removePotionEffect(PotionEffectType.POISON);
                entityInBeam.addPotionEffect(newPoisonEffect);

            }else{
                applyPoison(entityInBeam);
            }

        }

      }

    private void applyPoison(LivingEntity le){
        le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int) (poisonDuration * 20), poisonLevel - 1));
        Grimmoire.POISON.startPoisonEffect(le);
    }

}
