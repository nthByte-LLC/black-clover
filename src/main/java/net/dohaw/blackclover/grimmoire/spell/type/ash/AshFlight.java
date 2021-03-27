package net.dohaw.blackclover.grimmoire.spell.type.ash;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;

public class AshFlight extends CastSpellWrapper {

    public AshFlight(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ASH_FLIGHT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {



        return true;
    }

}
