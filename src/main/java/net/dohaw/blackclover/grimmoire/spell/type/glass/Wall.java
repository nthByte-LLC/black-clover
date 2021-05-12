package net.dohaw.blackclover.grimmoire.spell.type.glass;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a wall of glass in front of the caster
 */
public class Wall extends CastSpellWrapper {

    private List<List<Location>> allGlassWallsInWorld = new ArrayList<>();

    private double duration;
    private int wallHeight, wallWidth;

    public Wall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.WALL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        List<Location> wallBlockLocations = SpellUtils.makeWall(player.getLocation(), Material.GLASS, wallWidth, wallHeight, false);
        allGlassWallsInWorld.add(wallBlockLocations);

        SpellUtils.playSound(player, Sound.BLOCK_GLASS_PLACE);
        SpellUtils.spawnParticle(player, Particle.END_ROD, 30, 1, 1,1);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            allGlassWallsInWorld.remove(wallBlockLocations);
            removeWallBlocks(wallBlockLocations);
        }, (long) (duration * 20L));

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.wallHeight = grimmoireConfig.getIntegerSetting(KEY, "Wall Height");
        this.wallWidth = grimmoireConfig.getIntegerSetting(KEY, "Wall Width");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

    @Override
    public void prepareShutdown() {
        for (int i = 0; i < allGlassWallsInWorld.size(); i++) {
            List<Location> wallBlockLocations = allGlassWallsInWorld.remove(i);
            removeWallBlocks(wallBlockLocations);
        }
    }

    private void removeWallBlocks(List<Location> wallBlockLocations){
        wallBlockLocations.forEach(location -> location.getBlock().setType(Material.AIR));
    }

}
