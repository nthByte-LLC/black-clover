package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.spell.SpellType;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import java.util.List;

public class MultiFallingBlockRunner extends FallingBlockRunner{

    private List<FallingBlock> fallingBlocks;

    public MultiFallingBlockRunner(Player caster, FallingBlock parentBlock, List<FallingBlock> fallingBlocks, SpellType spell, double damage, boolean hasIntertia) {
        super(caster, parentBlock, spell, damage, hasIntertia);
        this.fallingBlocks = fallingBlocks;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        fallingBlocks.forEach(block -> {
            block.remove();
            block.getLocation().getBlock().setType(Material.AIR);
        });
    }

}
