package net.dohaw.blackclover.event;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpellDamageEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private boolean isCancelled;

    @Getter
    private Player damager;

    @Getter
    private Entity damaged;

    @Getter @Setter
    private double damage;

    @Getter
    private SpellType spell;

    public SpellDamageEvent(SpellType spell, double damage, Entity damaged, Player damager){
        this.spell = spell;
        this.damager = damager;
        this.damaged = damaged;
        this.damage = damage;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

}
