package net.dohaw.blackclover.grimmoire.spell.type.plant;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class LeafWall extends CastSpellWrapper {

    private int width;
    private int height;
    private double duration;
    private Material leafMaterial;

    public LeafWall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.LEAF_WALL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Location locInFront = LocationUtil.getLocationInFront(player, 2).add(0, 1, 0);
        int leftAmount = width % 2 == 0 ? width / 2 : (width - 1) / 2;
        Location startingWallLocation = LocationUtil.getLocationToLeft(locInFront, leftAmount);
        Location currentWallLocation = startingWallLocation.clone();
        List<Location> leafLocations = new ArrayList<>();

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){

                Block block = currentWallLocation.getBlock();
                // replaces air
                if(block.getType() == Material.AIR){
                    leafLocations.add(currentWallLocation.clone());
                    block.setType(leafMaterial);
                }
                currentWallLocation = currentWallLocation.add(0, 1, 0);

            }
            currentWallLocation = LocationUtil.getLocationToRight(startingWallLocation, x + 1);
        }

        SpellUtils.playSound(locInFront, Sound.BLOCK_GRASS_BREAK);
        SpellUtils.spawnParticle(locInFront, Particle.TOTEM, 10, 1, 1, 1);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            leafLocations.forEach(loc -> {
                loc.getBlock().setType(Material.AIR);
            });
            SpellUtils.spawnParticle(locInFront, Particle.SQUID_INK, 10, 1, 1, 1);
        }, (long) (duration * 20));

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
        this.width = grimmoireConfig.getIntegerSetting(KEY, "Width");
        this.height = grimmoireConfig.getIntegerSetting(KEY, "Height");
        String leafMaterialStr = grimmoireConfig.getStringSetting(KEY, "Leaf Material");
        if(leafMaterialStr == null){
            leafMaterialStr = "OAK_LEAVES";
        }
        this.leafMaterial = Material.valueOf(leafMaterialStr);
    }

    @Override
    public void prepareShutdown() {

    }

}
