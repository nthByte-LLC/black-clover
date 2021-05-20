package net.dohaw.blackclover.grimmoire.spell.type.rock;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Pebble extends CastSpellWrapper {

    private double forceMultiplier;
    private int damage;

    public Pebble(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PEBBLE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        SpellUtils.spawnFallingBlock(player, KEY, Material.CRACKED_STONE_BRICKS, forceMultiplier, damage, true);
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
