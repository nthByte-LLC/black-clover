package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class MultiFallingBlockRunner extends FallingBlockRunner{

    private List<FallingBlock> fallingBlocks;

    /**
     *
     * @param caster The caster of the spell
     * @param inertiaBlock If intertia is enabled, then this is the block that the player will repeatedly be teleported to during intertia {@link FallingBlockRunner}
     * @param fallingBlocks The falling blocks
     * @param spell The spell that was casted
     * @param damage The amount of damage that will be done
     * @param hasIntertia Whether the player will be moved with the blocks when they are hit
     */
    public MultiFallingBlockRunner(Player caster, FallingBlock inertiaBlock, List<FallingBlock> fallingBlocks, SpellType spell, double damage, boolean hasIntertia) {
        super(caster, inertiaBlock, spell, damage, hasIntertia);
        this.fallingBlocks = fallingBlocks;
    }

    @Override
    public void run() {
        FallingBlock firstBlock = fallingBlocks.get(0);
        if(!firstBlock.isOnGround()){
            for(FallingBlock block : fallingBlocks){
                checkBlockNearbyEntities(block);
            }
        }else{
            cancel();
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        // 1 more check
        for(FallingBlock block : fallingBlocks){
            checkBlockNearbyEntities(block);
        }
        fallingBlocks.forEach(block -> {
            block.remove();
            block.getLocation().getBlock().setType(Material.AIR);
        });
    }

}
