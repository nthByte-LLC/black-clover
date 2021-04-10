package net.dohaw.blackclover.grimmoire.spell.type.rock;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BlockSnapshot;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class Wall extends CastSpellWrapper {

    private List<List<Location>> allWallLocations = new ArrayList<>();

    private double duration;
    private int wallHeight;
    private int wallWidth;

    public Wall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.WALL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        List<Location> wallLocations = SpellUtils.makeWall(player.getLocation(), Material.CRACKED_STONE_BRICKS, wallWidth, wallHeight, false);
        allWallLocations.add(wallLocations);

        //Sets the blocks back to air
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            allWallLocations.remove(wallLocations);
            wallLocations.forEach(location -> location.getBlock().setType(Material.AIR));
        }, (long) (duration * 20));

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.wallWidth = grimmoireConfig.getIntegerSetting(KEY, "Wall Width");
        this.wallHeight = grimmoireConfig.getIntegerSetting(KEY, "Wall Height");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

    @Override
    public void prepareShutdown() {
        for(List<Location> wallLocations : allWallLocations){
            for(Location loc : wallLocations){
                loc.getBlock().setType(Material.AIR);
            }
        }
    }

}
