package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.PortalThresholdCrossEvent;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeGate extends PortalSpell{

    private Map<UUID, Portal> portals = new HashMap<>();

    public HomeGate(GrimmoireConfig grimmoireConfig) {
        super(SpellType.HOME_GATE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();
        Location bedLocation = player.getBedSpawnLocation();
        if(bedLocation == null){
            player.sendMessage("You have not slept in a bed recently!");
            return false;
        }

        if(portals.containsKey(player.getUniqueId())){
            Portal currentPortal = portals.get(player.getUniqueId());
            currentPortal.getPortalEnterChecker().cancel();
            currentPortal.getPortalDrawer().cancel();
        }

        Location portalStartLocation = getPortalStartLocation(player);
        portals.put(player.getUniqueId(), new BedPortal(portalStartLocation, this, player));

        return true;

    }

    @EventHandler
    @Override
    public void onEnterPortal(PortalThresholdCrossEvent e) {

        Entity entityEntered = e.getEntityEntered();
        Portal portalEntered = e.getPortalEntered();
        if(portalEntered instanceof BedPortal){
            BedPortal bedPortal = (BedPortal) portalEntered;
            if(!hasEnteredPortalRecently(entityEntered)){
                bedPortal.teleport(entityEntered);
                SpellUtils.playSound(entityEntered, Sound.ITEM_CHORUS_FRUIT_TELEPORT);
            }
        }

    }

}
