package net.dohaw.blackclover.runnable.spells.vortex;

import net.dohaw.blackclover.grimmoire.spell.type.vortex.Earthstorm;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class EarthstormRunner extends AbstractVortexTornadoRunner {

    private int slownessLevel;
    private double slownessDuration;
    private Player caster;

    public EarthstormRunner(LivingEntity entity, Earthstorm spell, Player caster) {
        super(entity, spell, new Particle.DustOptions(BukkitColor.BROWN, 1), 20L);
        this.caster = caster;
        this.slownessLevel = spell.getSlownessLevel();
        this.slownessDuration = spell.getSlownessDuration();
    }

    @Override
    public void doTornadoSpecifics() {
        Collection<Entity> entitiesNearTornado = getEntitiesNearTornado();
        for(Entity e : entitiesNearTornado){
            if(e instanceof LivingEntity){
                LivingEntity livingEntity = (LivingEntity) e;
                if(!livingEntity.getUniqueId().equals(caster.getUniqueId()) && livingEntity.getType() != EntityType.ARMOR_STAND){
                    SpellUtils.playSound(livingEntity, Sound.ENTITY_SQUID_SQUIRT);
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (slownessDuration * 20), slownessLevel - 1));
                }
            }
        }
    }

}
