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

import java.util.List;

public class Wall extends CastSpellWrapper {

    private double duration;
    private int wallHeight, wallWidth;

    public Wall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.WALL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        List<Location> wallBlockLocations = SpellUtils.makeWall(player.getLocation(), Material.GLASS, wallWidth, wallHeight, false);
        SpellUtils.playSound(player, Sound.BLOCK_GLASS_PLACE);
        SpellUtils.spawnParticle(player, Particle.END_ROD, 30, 1, 1,1);
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            wallBlockLocations.forEach(location -> location.getBlock().setType(Material.AIR));
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

    }

}
