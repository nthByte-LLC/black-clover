package net.dohaw.blackclover.grimmoire.spell.type.plant;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.LeafLadderRunner;
import net.dohaw.blackclover.util.BlockUtil;
import net.dohaw.corelib.ResponderFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public class LeafLadder extends CastSpellWrapper {

    public LeafLadder(GrimmoireConfig grimmoireConfig) {
        super(SpellType.LEAF_LADDER, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        PlayerInteractEvent pie = (PlayerInteractEvent) e;
        Block targetBlock = pie.getClickedBlock();
        ResponderFactory rf = new ResponderFactory(player);

        if(targetBlock != null){
            BlockFace faceClicked = pie.getBlockFace();
            boolean isValidBlocksAndFace = faceClicked != BlockFace.DOWN && targetBlock.getRelative(faceClicked).getType() == Material.AIR && faceClicked != BlockFace.UP;
            if(isValidBlocksAndFace){
                new LeafLadderRunner(targetBlock, faceClicked).runTaskTimer(Grimmoire.instance, 0, 10);
            }else{
                rf.sendMessage("You can't place a leaf ladder here!");
                return false;
            }
        }else{
            rf.sendMessage("You haven't punched any block!");
            return false;
        }
        return true;
    }

}

