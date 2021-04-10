package net.dohaw.blackclover.grimmoire.spell.type.rock;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.ShapeUtils;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

public class Rock extends CastSpellWrapper {

    private double forceMultiplier;
    private int damage;

    public Rock(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ROCK, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        Location startRock = player.getLocation().add(0, 5, 0);
        List<FallingBlock> fallingBlocks = ShapeUtils.createFallingBlockCube(startRock, Material.CRACKED_STONE_BRICKS, 3, 3, 3);
        SpellUtils.fireFallingBlocks(player, KEY, fallingBlocks, forceMultiplier, damage, true);
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.forceMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Force Multiplier");
        this.damage = grimmoireConfig.getIntegerSetting(KEY, "Damage");
    }

}
