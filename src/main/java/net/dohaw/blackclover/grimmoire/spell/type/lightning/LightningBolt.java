package net.dohaw.blackclover.grimmoire.spell.type.lightning;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;

public class LightningBolt extends CastSpellWrapper {

    public LightningBolt(GrimmoireConfig grimmoireConfig) {
        super(SpellType.LIGHTNING_BOLT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        return false;
    }

    @Override
    public void prepareShutdown() {

    }
}
