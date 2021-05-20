package net.dohaw.blackclover.grimmoire.spell.type.rock;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.MetroidRunner;
import net.dohaw.blackclover.runnable.spells.MultiFallingBlockRunner;
import net.dohaw.blackclover.util.ShapeUtils;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Metroid extends CastSpellWrapper {

    private double forceMultiplier;
    private int castDistance;
    private int width, height;
    private double damage;

    public Metroid(GrimmoireConfig grimmoireConfig) {
        super(SpellType.METROID, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, entityInSight)){

            Location targetLocation = entityInSight.getLocation();
            ThreadLocalRandom current = ThreadLocalRandom.current();
            double randomXAdditive = current.nextDouble(-5, 5);
            double randomZAdditive = current.nextDouble(-5, 5);
            Location metorStart = targetLocation.add(randomXAdditive, 10, randomZAdditive);

            List<FallingBlock> meteorBlocks = ShapeUtils.createFallingBlockCube(metorStart, Material.CRACKED_STONE_BRICKS, width, height, width);
            Vector dir = entityInSight.getLocation().clone().subtract(metorStart).toVector();
            Vector force = dir.multiply(forceMultiplier);

            new MetroidRunner(player, meteorBlocks.get(0), meteorBlocks,  damage).runTaskTimer(Grimmoire.instance, 0L, 1L);
            for(FallingBlock fBlock : meteorBlocks){
                fBlock.setDropItem(false);
                fBlock.setHurtEntities(false);
                fBlock.setVelocity(force);
            }
            return true;

        }

        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
        this.width = grimmoireConfig.getIntegerSetting(KEY, "Width");
        this.height = grimmoireConfig.getIntegerSetting(KEY, "Height");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.forceMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Force Multiplier");
    }
}
