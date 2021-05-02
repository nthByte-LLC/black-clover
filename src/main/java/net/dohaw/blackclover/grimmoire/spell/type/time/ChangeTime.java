package net.dohaw.blackclover.grimmoire.spell.type.time;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ChangeTime extends CastSpellWrapper {

    public ChangeTime(GrimmoireConfig grimmoireConfig) {
        super(SpellType.CHANGE_TIME, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        Player player = pd.getPlayer();
        World world = player.getWorld();
        return false;
    }

    @Override
    public void prepareShutdown() {

    }
}
