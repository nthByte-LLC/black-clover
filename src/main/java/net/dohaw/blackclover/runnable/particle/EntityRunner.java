package net.dohaw.blackclover.runnable.particle;

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

    @Override
    public void run() {
        for (Entity reliedEntity : reliedEntities) {
            if(!reliedEntity.isValid()){
                cancel();
                break;
            }
        }
    }

}
