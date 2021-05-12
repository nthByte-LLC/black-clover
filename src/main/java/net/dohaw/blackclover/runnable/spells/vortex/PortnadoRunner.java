package net.dohaw.blackclover.runnable.spells.vortex;

import net.dohaw.blackclover.grimmoire.spell.type.vortex.Portnado;
import net.dohaw.blackclover.grimmoire.spell.type.vortex.VortexSpell;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class PortnadoRunner extends AbstractVortexTornadoRunner{

    private int maxXAdditive, maxZAdditive;
    private Player caster;

    public PortnadoRunner(Entity entity, Portnado spell, Player caster) {
        super(entity, spell, new Particle.DustOptions(Color.PURPLE, 1), 20L);
        this.caster = caster;
        this.maxXAdditive = spell.getMaxXAdditive();
        this.maxZAdditive = spell.getMaxZAdditive();
    }

    @Override
    public void doTornadoSpecifics() {
        Collection<Entity> entitiesNearTornado = getEntitiesNearTornado();
        for(Entity e : entitiesNearTornado){
            if(e instanceof LivingEntity){
                LivingEntity livingEntity = (LivingEntity) e;
                if(!livingEntity.getUniqueId().equals(caster.getUniqueId()) && livingEntity.getType() != EntityType.ARMOR_STAND){
                    teleportToRandomLocation(livingEntity);
                }
            }
        }
    }

    private void teleportToRandomLocation(LivingEntity entityBeingTeleported){

        Location newEntityLocation = entityBeingTeleported.getLocation().clone();
        double distanceFromTornado = newEntityLocation.distance(entity.getLocation());
        while(distanceFromTornado < 3){

            ThreadLocalRandom current = ThreadLocalRandom.current();
            int randomXAdditive = current.nextInt(-maxXAdditive, maxXAdditive);
            int randomZAdditive = current.nextInt(-maxZAdditive, maxZAdditive);
            int newX = newEntityLocation.getBlockX() + randomXAdditive;
            int newZ = newEntityLocation.getBlockZ() + randomZAdditive;

            World entityWorld = entityBeingTeleported.getWorld();
            int highestY = entityWorld.getHighestBlockYAt(newX, newZ);

            newEntityLocation = new Location(entityWorld, newX, highestY, newZ);
            distanceFromTornado = newEntityLocation.distance(entity.getLocation());

        }
        SpellUtils.playSound(entityBeingTeleported, Sound.ITEM_CHORUS_FRUIT_TELEPORT);
        entityBeingTeleported.teleport(newEntityLocation);

    }

}
