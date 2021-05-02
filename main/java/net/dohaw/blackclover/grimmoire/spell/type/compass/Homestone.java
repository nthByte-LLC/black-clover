package net.dohaw.blackclover.grimmoire.spell.type.compass;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.menu.WaypointsMenu;
import net.dohaw.blackclover.playerdata.CompassPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Map;

/**
 * Teleport player to their desired waypoint
 */
public class Homestone extends CastSpellWrapper {

    public Homestone(GrimmoireConfig grimmoireConfig) {
        super(SpellType.HOMESTONE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        if(pd instanceof CompassPlayerData){

            Player player = pd.getPlayer();
            CompassPlayerData cpd = (CompassPlayerData) pd;
            Map<String, Location> waypoints = cpd.getWaypoints();
            WaypointsMenu waypointsMenu = new WaypointsMenu(waypoints, getNumMenuSlots(waypoints.size()));
            waypointsMenu.initializeItems(player);
            waypointsMenu.openInventory(player);

        }else{
            throw new UnexpectedPlayerData();
        }

        return false;

    }

    private int getNumMenuSlots(int numWaypoints){
        int slots = 9;
        while(slots < numWaypoints){
            slots += 9;
        }
        return slots;
    }

    @Override
    public void prepareShutdown() {

    }

}
