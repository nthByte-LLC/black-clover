package net.dohaw.blackclover.event;

import lombok.Getter;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpellOffCooldownEvent extends Event {

    @Getter
    private SpellType spellCasted;

    @Getter
    private Player caster;

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public SpellOffCooldownEvent(SpellType spellCasted, Player caster){
        this.spellCasted = spellCasted;
        this.caster = caster;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
