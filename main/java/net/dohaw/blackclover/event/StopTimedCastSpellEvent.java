package net.dohaw.blackclover.event;

import lombok.Getter;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StopTimedCastSpellEvent extends Event {

    public enum Cause{
        CANCELED_BY_DAMAGE,
        COMPLETE_CAST;
    }

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Getter
    private SpellType spell;

    @Getter
    private Player caster;

    @Getter
    private Cause cause;

    public StopTimedCastSpellEvent(Player caster, SpellType spell, Cause cause){
        this.spell = spell;
        this.caster = caster;
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
