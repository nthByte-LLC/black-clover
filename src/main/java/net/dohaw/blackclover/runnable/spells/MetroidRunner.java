package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import java.util.List;

public class MetroidRunner extends MultiFallingBlockRunner{

    /**
     * @param caster        The caster of the spell
     * @param inertiaBlock  If intertia is enabled, then this is the block that the player will repeatedly be teleported to during intertia {@link FallingBlockRunner}
     * @param fallingBlocks The falling blocks
     * @param damage        The amount of damage that will be done
     */
    public MetroidRunner(Player caster, FallingBlock inertiaBlock, List<FallingBlock> fallingBlocks, double damage) {
        super(caster, inertiaBlock, fallingBlocks, SpellType.METROID, damage, false);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        SpellUtils.playSound(block, Sound.ENTITY_GENERIC_EXPLODE);
        SpellUtils.spawnParticle(block, Particle.EXPLOSION_LARGE, 30, 1, 1, 1);
    }
}
