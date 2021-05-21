package net.dohaw.blackclover.grimmoire.spell.type.permeation;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Permeation extends ActivatableSpellWrapper implements Listener {

    /*
        Players that are permeating as well as the location at which they started permeating
     */
    private Map<UUID, Location> permeatingPlayers = new HashMap<>();

    public Permeation(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PERMEATION, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if(player.getGameMode() != GameMode.SPECTATOR && !permeatingPlayers.containsKey(playerUUID)) {
            permeatingPlayers.put(playerUUID, player.getLocation().clone());
            player.setGameMode(GameMode.SPECTATOR);
            return super.cast(e, pd);
        }

        return false;

    }

    @Override
    public void doRunnableTick(PlayerData caster) {

    }

    @Override
    public void deactiveSpell(PlayerData caster) {
        Player player = caster.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if(permeatingPlayers.containsKey(playerUUID)){
            player.setGameMode(GameMode.SURVIVAL);
            Location startLocation = permeatingPlayers.remove(playerUUID);
            player.teleport(startLocation);
        }
    }

    /*
        Prevents the player from using the spectator hotbar
     */
    @EventHandler
    public void onTeleportInSpectator(PlayerTeleportEvent e){
        Player player = e.getPlayer();
        if(e.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE && permeatingPlayers.containsKey(player.getUniqueId())){
            player.sendMessage("You can't do this right now!");
            e.setCancelled(true);
        }
    }

}
