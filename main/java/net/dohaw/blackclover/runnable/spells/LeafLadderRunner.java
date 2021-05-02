package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.scheduler.BukkitRunnable;

public class LeafLadderRunner extends BukkitRunnable {

    private BlockFace faceClicked;
    private Block targetBlock;
    private boolean shouldPlaceNextVine = true;

    public LeafLadderRunner(Block targetBlock, BlockFace faceClicked){
        this.targetBlock = targetBlock;
        this.faceClicked = faceClicked;
    }

    @Override
    public void run() {

        Block relativeBlock = targetBlock.getRelative(faceClicked);
        relativeBlock.setType(Material.VINE, false);
        MultipleFacing mf = (MultipleFacing) relativeBlock.getBlockData();
        mf.setFace(faceClicked.getOppositeFace(), true);
        relativeBlock.setBlockData(mf);

        SpellUtils.spawnParticle(targetBlock.getLocation(), Particle.VILLAGER_HAPPY,  10, 0, 0, 0);
        SpellUtils.playSound(targetBlock.getLocation(), Sound.BLOCK_GRASS_BREAK);

        targetBlock = targetBlock.getLocation().add(0, 1, 0).getBlock();
        shouldPlaceNextVine = targetBlock.getType().isSolid() && targetBlock.getRelative(faceClicked).getType() == Material.AIR;

        if(!shouldPlaceNextVine){
            cancel();
        }

    }
}
