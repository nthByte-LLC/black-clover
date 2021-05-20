package net.dohaw.blackclover.grimmoire.spell.type.rock;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.ShapeUtils;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

public class Stone extends CastSpellWrapper {

    private double damage;
    private double forceMultiplier;

    public Stone(GrimmoireConfig grimmoireConfig) {
        super(SpellType.STONE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd)  {

        Player player = pd.getPlayer();
        List<FallingBlock> fallingBlocks = ShapeUtils.createFallingBlockCube(player.getLocation().add(0, 3, 0), Material.CRACKED_STONE_BRICKS, 2, 2, 2);

        SpellUtils.spawnFallingBlocks(player, KEY, fallingBlocks, forceMultiplier, damage, true);
        SpellUtils.playSound(player, Sound.BLOCK_STONE_PLACE);
        SpellUtils.spawnParticle(player, Particle.BLOCK_CRACK, Material.STONE.createBlockData(), 20, 1, 1, 1);

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.forceMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Force Multiplier");
        this.damage = grimmoireConfig.getIntegerSetting(KEY, "Damage");
    }

}
