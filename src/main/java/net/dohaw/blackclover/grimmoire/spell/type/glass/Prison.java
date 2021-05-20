package net.dohaw.blackclover.grimmoire.spell.type.glass;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.PersistableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.ShapeUtils;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a cage around the targeted enemy
 */
public class Prison extends CastSpellWrapper implements PersistableSpell {

    private List<List<Location>> allCageWallBlockLocations = new ArrayList<>();

    private double duration;
    private int castDistance;
    private int wallWidth, wallHeight;

    public Prison(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PRISON, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player caster = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, caster, castDistance);
        if(SpellUtils.isTargetValid(caster, entityInSight)){

            assert entityInSight != null;

            Location originWall = entityInSight.getLocation();
            for(int i = 0; i < 4; i++){

                originWall.setYaw(i * 90);
                List<Location> wallBlockLocations = SpellUtils.makeWall(originWall.clone(), Material.GLASS, wallWidth, wallHeight, true);
                allCageWallBlockLocations.add(wallBlockLocations);

                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                    allCageWallBlockLocations.remove(wallBlockLocations);
                    removeWallBlocks(wallBlockLocations);
                }, (long) (duration * 20));

            }

            SpellUtils.playSound(entityInSight, Sound.BLOCK_GLASS_PLACE);
            return true;

        }
        return false;

    }

    @Override
    public void prepareShutdown() {
        for (int i = 0; i < allCageWallBlockLocations.size(); i++) {
            List<Location> wallBlockLocations = allCageWallBlockLocations.remove(i);
            removeWallBlocks(wallBlockLocations);
        }
    }

    private void removeWallBlocks(List<Location> wallBlockLocations){
        wallBlockLocations.forEach(location -> location.getBlock().setType(Material.AIR));
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.wallWidth = grimmoireConfig.getIntegerSetting(KEY, "Wall Width");
        this.wallHeight = grimmoireConfig.getIntegerSetting(KEY, "Wall Height");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

}
