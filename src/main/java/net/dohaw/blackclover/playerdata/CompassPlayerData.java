package net.dohaw.blackclover.playerdata;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class CompassPlayerData extends PlayerData implements SpecifiableData{

    private LinkedList<Location> path = new LinkedList<>();

    private boolean isMakingPath;

    private List<BukkitTask> waypointParticleDrawers = new ArrayList<>();

    private Map<String, Location> waypoints = new HashMap<>();

    public CompassPlayerData(UUID uuid) {
        super(uuid, Grimmoire.COMPASS);
    }

    @Override
    public void saveSpecifiedData(FileConfiguration config) {
        waypoints.forEach((name, location) -> {
            config.set("Waypoints." + name, location);
        });
    }

    @Override
    public void loadSpecifiedData(FileConfiguration config) {
        Map<String, Location> waypointsMap = new HashMap<>();
        ConfigurationSection waypointsSection = config.getConfigurationSection("Waypoints");
        if(waypointsSection != null){
            config.getConfigurationSection("Waypoints").getKeys(false).forEach(name -> {
                waypointsMap.put(name, config.getLocation("Waypoints." + name));
            });
            this.waypoints = waypointsMap;
        }
    }

    public Map<String, Location> getWaypoints() {
        return waypoints;
    }

    public boolean isMakingPath() {
        return isMakingPath;
    }

    public void setMakingPath(boolean makingPath) {
        isMakingPath = makingPath;
    }

    public LinkedList<Location> getPath() {
        return path;
    }

}
