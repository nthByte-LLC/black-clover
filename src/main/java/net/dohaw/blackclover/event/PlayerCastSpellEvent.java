package net.dohaw.blackclover.event;

import lombok.Getter;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCastSpellEvent extends Event implements Cancellable {

    private boolean isCancelled = false;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Getter
    private PlayerData playerData;

    @Getter
    private SpellWrapper spellCasted;

    public PlayerCastSpellEvent(PlayerData playerData, SpellWrapper spellCasted){
        this.playerData = playerData;
        this.spellCasted = spellCasted;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
