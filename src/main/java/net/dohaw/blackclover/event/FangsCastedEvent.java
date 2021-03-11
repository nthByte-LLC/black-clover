package net.dohaw.blackclover.event;

import lombok.Getter;
import net.dohaw.blackclover.playerdata.ShakudoPlayerData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FangsCastedEvent extends Event {

    @Getter
    private ShakudoPlayerData caster;

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public FangsCastedEvent(ShakudoPlayerData caster){
        this.caster = caster;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
