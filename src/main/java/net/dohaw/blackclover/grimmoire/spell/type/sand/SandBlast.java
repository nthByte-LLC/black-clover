package net.dohaw.blackclover.grimmoire.spell.type.sand;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;

public class SandBlast extends SpellWrapper {

    public SandBlast(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SAND_BLAST, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        return false;
    }
}
