package net.dohaw.blackclover.event;

import net.dohaw.blackclover.grimmoire.spell.type.spatial.Portal;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PortalThresholdCrossEvent<T extends Portal> extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private Entity entityEntered;
    private T portalEntered;

    public PortalThresholdCrossEvent(Entity entityEntered, T portalEntered){
        this.entityEntered = entityEntered;
        this.portalEntered = portalEntered;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Entity getEntityEntered() {
        return entityEntered;
    }

    public T getPortalEntered() {
        return portalEntered;
    }

}
