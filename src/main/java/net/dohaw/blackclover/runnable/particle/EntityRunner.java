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
        new BukkitRunnable(){
            @Override
            public void run() {
                for (Entity reliedEntity : reliedEntities) {
                    if(!reliedEntity.isValid()){
                        EntityRunner.this.cancel();
                        this.cancel();
                        break;
                    }
                }
            }
        }.runTaskTimer(Grimmoire.instance, 1L, 20L);
    }

}
