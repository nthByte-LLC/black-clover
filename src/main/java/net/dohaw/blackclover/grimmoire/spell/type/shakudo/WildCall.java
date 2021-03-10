package net.dohaw.blackclover.grimmoire.spell.type.shakudo;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.ShakudoPlayerData;
import org.bukkit.event.Event;

public class WildCall extends CastSpellWrapper {

    public WildCall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.WILD_CALL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        ShakudoPlayerData spd = (ShakudoPlayerData) pd;
        return true;
    }
}
