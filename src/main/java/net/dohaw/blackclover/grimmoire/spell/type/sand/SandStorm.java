package net.dohaw.blackclover.grimmoire.spell.type.sand;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;

public class SandStorm extends CastSpellWrapper {

    public SandStorm(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SAND_STORM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        return true;
    }
}
