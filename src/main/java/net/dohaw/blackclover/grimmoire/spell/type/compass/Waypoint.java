package net.dohaw.blackclover.grimmoire.spell.type.compass;

import net.dohaw.blackclover.WaypointNamePrompt;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.CompassPlayerData;
import org.bukkit.Location;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Map;

public class Waypoint extends CastSpellWrapper {

    private int maxWaypoints;

    public Waypoint(GrimmoireConfig grimmoireConfig) {
        super(SpellType.WAYPOINT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        if(pd instanceof CompassPlayerData){

            Player player = pd.getPlayer();
            CompassPlayerData wpd = (CompassPlayerData) pd;
            Map<String, Location> waypoints = wpd.getWaypoints();

            Location closeWaypoint = getCloseWaypoint(player, waypoints);
            // is trying to delete a waypoint
            if(closeWaypoint != null && player.isSneaking()){
                waypoints.remove(closeWaypoint);
                player.sendMessage("This waypoint has been removed!");
            }else if(maxWaypoints != waypoints.size()){
                ConversationFactory cf = new ConversationFactory(Grimmoire.instance);
                Conversation conv = cf.withFirstPrompt(new WaypointNamePrompt(player, waypoints)).withEscapeSequence("/exit").withLocalEcho(false).buildConversation(player);
                conv.begin();
            }else{
                player.sendMessage("You have already set the maximum amount of waypoints!");
            }

            return true;

        }else{
            throw new UnexpectedPlayerData();
        }

    }

    private Location getCloseWaypoint(Player player, Map<String, Location> waypoints){
        for(Location waypoint : waypoints.values()){
            if(waypoint.distance(player.getLocation()) <= 2){
                return waypoint;
            }
        }
        return null;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.maxWaypoints = grimmoireConfig.getIntegerSetting(KEY, "Max Waypoints");
    }

}
