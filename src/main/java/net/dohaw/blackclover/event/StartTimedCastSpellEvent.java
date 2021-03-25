package net.dohaw.blackclover.event;

import lombok.Getter;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StartTimedCastSpellEvent extends Event{

        private static final HandlerList HANDLERS_LIST = new HandlerList();

        @Getter
        private SpellType spell;

        @Getter
        private Player caster;

        public StartTimedCastSpellEvent(Player caster, SpellType spell){
            this.spell = spell;
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
