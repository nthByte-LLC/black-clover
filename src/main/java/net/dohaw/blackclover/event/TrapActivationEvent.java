package net.dohaw.blackclover.event;

import net.dohaw.blackclover.grimmoire.spell.type.trap.Trap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TrapActivationEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private LivingEntity trapStepper;
    private Trap trap;

    public TrapActivationEvent(Trap trap, LivingEntity trapStepper){
        this.trap = trap;
        this.trapStepper = trapStepper;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public LivingEntity getTrapStepper() {
        return trapStepper;
    }

    public Trap getTrap() {
        return trap;
    }

}
