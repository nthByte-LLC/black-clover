package net.dohaw.blackclover.runnable.particle;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Defines a class that relies on an entity being alive in order to function
 * Cancels if the entity dies or de-spawns.
 */
public abstract class EntityRunner extends BukkitRunnable {

    protected Entity[] reliedEntities;

    public EntityRunner(Entity ...reliedEntities){
        this.reliedEntities = reliedEntities;
    }

    /**
     * Should be used in every single run method that extends this. If the entities aren't valid, then we need to not run everything else in the run method.
     */
    protected boolean areEntitiesValid(){
        for (Entity reliedEntity : reliedEntities) {
            if(!reliedEntity.isValid()){
                cancel();
                return false;
            }
        }
        return true;
    }

}
