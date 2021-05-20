package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.PortalThresholdCrossEvent;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Banish extends PortalSpell<FloorPortal> {

    private Map<UUID, FloorPortal> portals = new HashMap<>();

    private World netherWorld;

    public Banish(GrimmoireConfig grimmoireConfig) {
        super(SpellType.BANISH, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();
        if(netherWorld == null){
            player.sendMessage("The nether world isn't set properly in the config! You can't make this portal right now...");
            return false;
        }

        if(portals.containsKey(player.getUniqueId())){
            FloorPortal currentPortal = portals.get(player.getUniqueId());
            currentPortal.stopPortal();
        }

        Location portalStartLocation = getPortalStartLocation(player);
        portals.put(player.getUniqueId(), new FloorPortal(portalStartLocation, this, new Particle.DustOptions(Color.BLACK, 1)));

        return true;

    }

    @Override
    public void prepareShutdown() {

    }

    @EventHandler
    @Override
    public void onEnterPortal(PortalThresholdCrossEvent<FloorPortal> e) {

        Entity entityEntered = e.getEntityEntered();

        if(!hasEnteredPortalRecently(entityEntered)){
            entityEntered.teleport(netherWorld.getSpawnLocation());
            SpellUtils.playSound(entityEntered, Sound.ITEM_CHORUS_FRUIT_TELEPORT);
        }

    }

    @Override
    public void loadSettings() {

        super.loadSettings();

        String netherWorldName = grimmoireConfig.getStringSetting(KEY, "Nether World Name");
        if(netherWorldName == null || Bukkit.getWorld(netherWorldName) == null){
            netherWorld = null;
            return;
        }
        this.netherWorld = Bukkit.getWorld(netherWorldName);

    }

}
