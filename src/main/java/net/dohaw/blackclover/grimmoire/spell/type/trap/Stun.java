package net.dohaw.blackclover.grimmoire.spell.type.trap;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Stun extends TrapSpell {

    private Map<UUID, Location> carpetLocations = new HashMap<>();

    public Stun(GrimmoireConfig grimmoireConfig) {
        super(SpellType.STUN, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Block playerCurrentStandingBlock = player.getLocation().getBlock();
        playerCurrentStandingBlock.setType(Material.BLACK_CARPET);

        carpetLocations.put(player.getUniqueId(), playerCurrentStandingBlock.getLocation());

        return true;
    }

    @Override
    public void prepareShutdown() {
        
    }
}
