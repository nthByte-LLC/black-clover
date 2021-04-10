package net.dohaw.blackclover.grimmoire.spell.type.sand;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class SandBlast extends CastSpellWrapper {

    private double forceMultiplier;
    private double damage;

    public SandBlast(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SAND_BLAST, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        SpellUtils.fireFallingBlock(player, KEY, Material.SAND, forceMultiplier, damage, true);
        deductMana(pd);
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.forceMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Force Multiplier");
        this.damage = grimmoireConfig.getIntegerSetting(KEY, "Damage");
    }

    @Override
    public void prepareShutdown() {

    }
}
