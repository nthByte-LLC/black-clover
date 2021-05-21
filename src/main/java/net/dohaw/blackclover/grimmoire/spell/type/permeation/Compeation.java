package net.dohaw.blackclover.grimmoire.spell.type.permeation;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.HashSet;
import java.util.UUID;

public class Compeation extends CastSpellWrapper {

    private HashSet<UUID> canGoThroughWalls = new HashSet<>();

    public Compeation(GrimmoireConfig grimmoireConfig) {
        super(SpellType.COMPEATION, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();
        if(canGoThroughWalls.contains(pd.getUUID())) {
            player.sendMessage("You can already go through walls!");
            return false;
        }

        return true;
    }

}
