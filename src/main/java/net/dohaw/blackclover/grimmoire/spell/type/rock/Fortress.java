package net.dohaw.blackclover.grimmoire.spell.type.rock;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class Fortress extends CastSpellWrapper {

    private double duration;

    private int wallWidth, wallHeight;
    private List<List<Location>> allWallLocations = new ArrayList<>();

    public Fortress(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FORTRESS, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Location originWall = pd.getPlayer().getLocation();
        for(int i = 0; i < 4; i++){

            originWall.setYaw(i * 90);
            List<Location> wall = SpellUtils.makeWall(originWall.clone(), Material.CRACKED_STONE_BRICKS, wallWidth, wallHeight, true);
            allWallLocations.add(wall);

            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                allWallLocations.remove(wall);
                wall.forEach(loc -> loc.getBlock().setType(Material.AIR));
            }, (long) (duration * 20));

        }

        return true;

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.wallWidth = grimmoireConfig.getIntegerSetting(KEY, "Wall Width");
        this.wallHeight = grimmoireConfig.getIntegerSetting(KEY, "Wall Height");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }
}
