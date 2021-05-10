package net.dohaw.blackclover.runnable.spells.vortex;

import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.vortex.Tornado;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;

public class VortexTornadoSpell extends AbstractVortexTornado {

    private Player caster;
    private double damage;
    private double forceMultiplier;

    public VortexTornadoSpell(Entity entity, Particle.DustOptions dustOptions, Tornado spell, Player caster) {
        super(entity, dustOptions, spell.getMaxTravelDistance(), 20L);
        this.forceMultiplier = spell.getForceMultiplier();
        this.damage = spell.getDamage();
        this.caster = caster;
    }

    @Override
    public void doTornadoSpecifics() {

        TornadoParticleRunner firstTornado = tornadoes.get(0);
        // Gets the entities as high as the tornado and 2 blocks in each x and y direction.
        Collection<Entity> entitiesNearTornado = entity.getNearbyEntities(2, firstTornado.yIncrease, 2);
        
        for(Entity e : entitiesNearTornado){
            if(e instanceof LivingEntity){
                LivingEntity willBePushed = (LivingEntity) e;
                willBePushed.setVelocity(entity.getLocation().toVector().subtract(willBePushed.getLocation().toVector()).normalize().multiply(-forceMultiplier));
                SpellUtils.doSpellDamage(willBePushed, caster, SpellType.TORNADO, damage);
            }
        }

    }

}
