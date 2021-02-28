package net.dohaw.blackclover.grimmoire.spell.type.sand;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extension.factory.PatternFactory;
import com.sk89q.worldedit.extension.factory.parser.pattern.SingleBlockPatternParser;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DamageableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class SandGrave extends CastSpellWrapper implements DamageableSpell {

    private int castDistance;
    private int radiusBall;
    private int blocksAbovePlayer;

    public SandGrave(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SAND_GRAVE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityHit = SpellUtils.getEntityInLineOfSight(player, castDistance);
        if(entityHit != null){
            System.out.println("NOT NULL");
            Location centerBall = entityHit.getLocation().add(0, blocksAbovePlayer + radiusBall, 0);
            try {
                spawnSandBall(player, centerBall);
            } catch (MaxChangedBlocksException inputParseException) {
                inputParseException.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getNumberSetting(KEY, "Cast Distance");
        this.blocksAbovePlayer = grimmoireConfig.getNumberSetting(KEY, "Blocks Above Player");
        this.radiusBall = grimmoireConfig.getNumberSetting(KEY, "Radius Ball");
    }

    public void spawnSandBall(Player p, Location center) throws MaxChangedBlocksException {
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(new BukkitWorld(p.getWorld()))) {
            System.out.println("HERE");
            editSession.makeSphere(BlockVector3.at(center.getX(), center.getY(), center.getZ()), BlockTypes.STONE.getDefaultState(), radiusBall, true);
        }
    }

//    public void spawnSandBall(Player p, Location center, double density) {
//        for (double x = -radiusBall; x < radiusBall; x += density) {
//            for (double z = -radiusBall; z < radiusBall; z += density) {
//                double y = Math.sqrt(radiusBall*radiusBall - x*x - z*z);
//                Location loc1 = center.clone().subtract(-x, y, -z);
//                Location loc2 = center.clone().add(-x, y, -z);
//                p.getWorld().getBlockAt(loc1).setType(Material.STONE);
//                p.getWorld().getBlockAt(loc2).setType(Material.STONE);
//            }
//        }
//    }

}
