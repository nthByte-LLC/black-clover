package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.spell.SpellType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MultiFallingBlockRunner extends FallingBlockRunner{

    private List<FallingBlock> fallingBlocks = new ArrayList<>();

    public MultiFallingBlockRunner(Player caster, FallingBlock parentBlock, SpellType spell, double damage, boolean hasIntertia) {
        super(caster, parentBlock, spell, damage, hasIntertia);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        fallingBlocks.forEach(Entity::remove);
    }
}
