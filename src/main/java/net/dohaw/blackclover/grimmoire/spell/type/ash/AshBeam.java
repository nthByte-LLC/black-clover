package net.dohaw.blackclover.grimmoire.spell.type.ash;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;

public class AshBeam extends CastSpellWrapper {

    public AshBeam(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ASH_BEAM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        return false;
    }
}
