package net.dohaw.blackclover.grimmoire.spell.type.glass;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Periodically turns the player's target block into glass if the target block is sand.
 */
public class GlassCreation extends ActivatableSpellWrapper {

    public GlassCreation(GrimmoireConfig grimmoireConfig) {
        super(SpellType.GLASS_CREATION, grimmoireConfig);
    }

    @Override
    public void doRunnableTick(PlayerData caster) {

        Player player = caster.getPlayer();
        Block targetBlock = player.getTargetBlockExact(5);
        if(targetBlock != null){
            if(targetBlock.getType() == Material.SAND){
                targetBlock.setType(Material.GLASS);
                SpellUtils.playSound(targetBlock, Sound.BLOCK_LAVA_EXTINGUISH);
                SpellUtils.spawnParticle(targetBlock.getLocation(), Particle.REDSTONE, new Particle.DustOptions(Color.BLACK, 1), 30, 0.2f, 0.2f, 0.2f);
            }
        }else{
            SpellUtils.playSound(player, Sound.ITEM_SHIELD_BLOCK);
        }

    }

    @Override
    public void deactiveSpell(PlayerData caster) { }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public long getRunnableInterval() {
        return 15L;
    }

}
