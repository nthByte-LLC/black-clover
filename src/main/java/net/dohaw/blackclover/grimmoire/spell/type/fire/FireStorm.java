package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;

public class FireStorm extends CastSpellWrapper {

    public FireStorm(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_STORM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        return false;
    }

}
