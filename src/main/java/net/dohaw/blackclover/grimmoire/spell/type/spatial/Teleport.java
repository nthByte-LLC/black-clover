package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.PortalThresholdCrossEvent;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.menu.PlayerTeleportMenu;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Teleport extends PortalSpell<StandingPortal> {

    private Map<UUID, StandingPortal> portals = new HashMap<>();

    public Teleport(GrimmoireConfig grimmoireConfig) {
        super(SpellType.TELEPORT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();
        PlayerTeleportMenu menu = new PlayerTeleportMenu(Grimmoire.instance, this);
        menu.initializeItems(player);
        menu.openInventory(player);

        return false;

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.widthPortal = grimmoireConfig.getDoubleSetting(KEY, "Width Portal");
        this.heightPortal = grimmoireConfig.getDoubleSetting(KEY, "Height Portal");
    }

    @EventHandler
    public void onEnterPortal(PortalThresholdCrossEvent<StandingPortal> e) {

        Entity entityEntered = e.getEntityEntered();
        Portal portalEntered = e.getPortalEntered();
        if(e.getPortalEntered() instanceof PlayerPortal){
            PlayerPortal playerPortal = (PlayerPortal) portalEntered;
            if(!hasEnteredPortalRecently(entityEntered)){
                Player destinationPlayer = playerPortal.getDestinationPlayer();
                if(!entityEntered.getUniqueId().equals(destinationPlayer.getUniqueId())){
                    portalEntered.teleport(entityEntered);
                    SpellUtils.playSound(entityEntered, Sound.ITEM_CHORUS_FRUIT_TELEPORT);
                    startPortalEnteringCooldown(entityEntered);
                }
            }
        }

    }

    public Map<UUID, StandingPortal> getPortals() {
        return portals;
    }

}
