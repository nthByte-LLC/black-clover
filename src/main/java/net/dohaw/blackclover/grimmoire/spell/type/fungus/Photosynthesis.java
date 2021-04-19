package net.dohaw.blackclover.grimmoire.spell.type.fungus;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Photosynthesis extends CastSpellWrapper {

    public Photosynthesis(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PHOTOSYNTHESIS, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        byte lightLevel = player.getLocation().getBlock().getLightFromSky();
        return false;
    }

    @Override
    public void prepareShutdown() {

    }
}
