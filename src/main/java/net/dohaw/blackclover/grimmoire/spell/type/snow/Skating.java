package net.dohaw.blackclover.grimmoire.spell.type.snow;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;

public class Skating extends CastSpellWrapper {

    public Skating(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SKATING, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        return true;
    }
}
