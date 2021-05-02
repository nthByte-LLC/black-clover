package net.dohaw.blackclover.event;

import lombok.Getter;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CreatePlayerDataEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Getter
    private PlayerData playerData;

    public CreatePlayerDataEvent(PlayerData playerData){
        this.playerData = playerData;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }


}
