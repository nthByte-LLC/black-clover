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


public class Portals extends PortalSpell {

    private Map<UUID, PortalLink> portalLinks = new HashMap<>();

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
        Location portalStartLocation = getPortalStartLocation(player);

        Portal createdPortal = new Portal(portalStartLocation, widthPortal, heightPortal, isSettingFirstPortal);
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
        if(!hasEnteredPortalRecently(entityEntered)){

            Portal portalEntered = e.getPortalEntered();
            Portal potentialLink = getLink(portalEntered);
            if(potentialLink != null){

                potentialLink.teleportEntityToThisPortal(entityEntered);
                hasRecentlyEnteredPortal.add(entityEntered.getUniqueId());
                startPortalEnteringCooldown(entityEntered);
                SpellUtils.playSound(entityEntered, Sound.ITEM_CHORUS_FRUIT_TELEPORT);

            }

        }

    }

}
