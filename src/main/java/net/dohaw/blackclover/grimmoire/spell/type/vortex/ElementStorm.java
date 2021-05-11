package net.dohaw.blackclover.grimmoire.spell.type.vortex;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;

public class ElementStorm extends VortexSpell{

    public ElementStorm(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ELEMENT_STORM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        return false;
    }

    @Override
    public void prepareShutdown() {

    }
}
