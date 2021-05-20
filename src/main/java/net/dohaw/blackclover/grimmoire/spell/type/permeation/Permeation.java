package net.dohaw.blackclover.grimmoire.spell.type.permeation;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashSet;
import java.util.UUID;

public class Permeation extends ActivatableSpellWrapper implements Listener {

    private HashSet<UUID> permeatingPlayers = new HashSet<>();

    public Permeation(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PERMEATION, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();
        if(player.getGameMode() != GameMode.SPECTATOR && !permeatingPlayers.contains(player.getUniqueId())){
            player.setGameMode(GameMode.SPECTATOR);
            return super.cast(e, pd);
        }

        return false;

    }

    @Override
    public void doRunnableTick(PlayerData caster) {

    }

    @Override
    public void deactiveSpell(PlayerData caster) throws UnexpectedPlayerData {
        Player player = caster.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);
    }

    @Override
    public void prepareShutdown() {

    }

    /*
        Prevents the player from using the spectator hotbar
     */
    @EventHandler
    public void onTeleportInSpectator(PlayerTeleportEvent e){
        Player player = e.getPlayer();
        if(e.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE && permeatingPlayers.contains(player.getUniqueId())){
            e.setCancelled(true);
        }
    }

}
