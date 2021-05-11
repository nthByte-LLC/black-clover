package net.dohaw.blackclover.runnable.spells.vortex;

import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.vortex.Tornado;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;

public class VortexTornadoRunner extends AbstractVortexTornadoRunner {

    private Player caster;
    private double damage;
    private double forceMultiplier;

    public VortexTornadoRunner(Entity entity, Tornado spell, Player caster) {
        super(entity, new Particle.DustOptions(BukkitColor.DARK_GREY, 1), spell.getTornadoMaxTravelDistance(), 20L);
        this.forceMultiplier = spell.getForceMultiplier();
        this.damage = spell.getDamage();
        this.caster = caster;
    }

    @Override
    public void doTornadoSpecifics() {

        Collection<Entity> entitiesNearTornado = getEntitiesNearTornado();
        for(Entity e : entitiesNearTornado){
            if(e instanceof LivingEntity){
                LivingEntity willBePushed = (LivingEntity) e;
                if(!willBePushed.getUniqueId().equals(caster.getUniqueId()) && willBePushed.getType() != EntityType.ARMOR_STAND){
                    willBePushed.setVelocity(entity.getLocation().toVector().subtract(willBePushed.getLocation().toVector()).normalize().multiply(-forceMultiplier));
                    SpellUtils.doSpellDamage(willBePushed, caster, SpellType.TORNADO, damage);
                }
            }
        }

    }

}
