package net.dohaw.blackclover.event;

import lombok.Getter;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PostStopActiveSpellEvent extends Event {

    public enum Cause{
        SELF_STOP,
        FORCEFUL_STOP,
    }

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Getter
    private SpellType spell;

    @Getter
    private Player caster, stopper;

    @Getter
    private Cause cause;

    public PostStopActiveSpellEvent(SpellType spell, Player caster, Player stopper, Cause cause){
        this.spell = spell;
        this.caster = caster;
        this.stopper = stopper;
        this.cause = cause;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
