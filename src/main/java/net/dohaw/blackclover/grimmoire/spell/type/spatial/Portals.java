package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.PortalThresholdCrossEvent;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class Portals extends CastSpellWrapper implements Listener {

    private double widthPortal, heightPortal;

    private Map<UUID, PortalLink> portalLinks = new HashMap<>();

    private HashSet<UUID> hasRecentlyEnteredPortal = new HashSet<>();

    public Portals(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PORTALS, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();
        PortalLink currentPortalLink;

        UUID playerUUID = player.getUniqueId();
        if(portalLinks.containsKey(playerUUID)){
            currentPortalLink = portalLinks.get(playerUUID);
        }else{
            currentPortalLink = new PortalLink();
        }

        boolean isSettingFirstPortal = !player.isSneaking();
        Portal createdPortal = createPortal(player.getLocation(), isSettingFirstPortal);
        currentPortalLink.setPortal(createdPortal, isSettingFirstPortal);

        if(isSettingFirstPortal){
            player.sendMessage("You have set the first portal!");
        }else{
            player.sendMessage("You have set the second portal!");
        }

        if(currentPortalLink.isLinked()){
            player.sendMessage("The two portals have been linked together!");
        }

        portalLinks.put(player.getUniqueId(), currentPortalLink);

        return true;
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.widthPortal = grimmoireConfig.getDoubleSetting(KEY, "Width Portal");
        this.heightPortal = grimmoireConfig.getDoubleSetting(KEY, "Height Portal");
    }

    private Portal createPortal(Location playerLocation, boolean isFirstPortal){
        Location locInFront = LocationUtil.getAbsoluteLocationInFront(playerLocation.add(0, 0.1, 0), 1);
        Location bottomLeftCorner = LocationUtil.getAbsoluteLocationToLeft(locInFront, widthPortal / 2);
        return new Portal(bottomLeftCorner, widthPortal, heightPortal, isFirstPortal);
    }

    private Portal getLink(Portal portal){

        for(PortalLink link : portalLinks.values()){
            Portal potentialLink = link.getLink(portal);
            if(potentialLink != null){
                return potentialLink;
            }
        }

        return null;

    }

    @EventHandler
    public void onEnterPortal(PortalThresholdCrossEvent e){

        Entity entityEntered = e.getEntityEntered();

        UUID entityUUID = entityEntered.getUniqueId();
        if(!hasRecentlyEnteredPortal.contains(entityUUID)){

            Portal portalEntered = e.getPortalEntered();
            Portal potentialLink = getLink(portalEntered);
            if(potentialLink != null){

                potentialLink.teleportEntityToThisPortal(entityEntered);
                hasRecentlyEnteredPortal.add(entityUUID);

                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                    hasRecentlyEnteredPortal.remove(entityUUID);
                }, 20L * 5);

            }

        }

    }

}
