package net.dohaw.blackclover.grimmoire.spell.type.sand;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BlockSnapshot;
import net.dohaw.blackclover.util.ShapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

public class Earthquake extends CastSpellWrapper {

    private double duration;
    private int radius;

    public Earthquake(GrimmoireConfig grimmoireConfig) {
        super(SpellType.EARTHQUAKE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Location playerLocation = player.getLocation();
        int originX = playerLocation.getBlockX();
        int originY = playerLocation.getBlockY() - 1;
        int originZ = playerLocation.getBlockZ();
        List<BlockSnapshot> blocksInRadius = ShapeUtils.getBlockSnapshotsInCube(player.getLocation(), radius);

        // side 1
        Location side1Start = new Location(player.getWorld(), originX - radius, originY, originZ - radius);
        for(int additive = 0; additive < (radius * 2); additive++){
            Location loc = side1Start.clone().add(additive, 0, 0);
            loc.getBlock().setType(Material.DIAMOND_BLOCK);
        }

        // side 2
        Location side2Start = new Location(player.getWorld(), originX + radius, originY, originZ - radius);
        for(int additive = 0; additive < (radius * 2); additive++){
            Location loc = side2Start.clone().add(0, 0, additive);
            loc.getBlock().setType(Material.GOLD_BLOCK);
        }

        // side 3
        // added 1 to cover up for missed corner
        Location side3Start = new Location(player.getWorld(), originX - radius, originY, originZ + radius);
        for(int additive = 0; additive < ((radius * 2 )+ 1); additive++){
            Location loc = side3Start.clone().add(additive, 0, 0);
            loc.getBlock().setType(Material.IRON_BLOCK);
        }

        Location side4Start = new Location(player.getWorld(), originX - radius, originY, originZ - radius);
        for(int additive = 0; additive < (radius * 2); additive++){
            Location loc = side4Start.clone().add(0, 0, additive);
            loc.getBlock().setType(Material.EMERALD_BLOCK);
        }

//        Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
//
//
//
//        }, 0L, (long) duration);


        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getNumberSetting(KEY, "Radius");
        this.duration = grimmoireConfig.getNumberSetting(KEY, "Duration");
    }
}
