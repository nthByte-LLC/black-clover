package net.dohaw.blackclover.grimmoire.spell.type.glass;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.ShieldRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * A glass wall constantly appears in the direction that the player is looking. Very similar to WaterWall in the Water Magic grimmoire
 */
public class Shield extends CastSpellWrapper {

    private int shieldWidth, shieldHeight;
    private double duration;

    public Shield(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SHIELD, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();
        ShieldRunner shieldRunner = new ShieldRunner(player, this);
        shieldRunner.runTaskTimer(Grimmoire.instance, 0L, 20L);

        SpellUtils.playSound(player, Sound.ITEM_SHIELD_BLOCK);
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, shieldRunner::cancel, (long) (duration * 20));

        return true;

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
        this.shieldWidth = grimmoireConfig.getIntegerSetting(KEY, "Shield Width");
        this.shieldHeight = grimmoireConfig.getIntegerSetting(KEY, "Shield Height");
    }

    public int getShieldWidth() {
        return shieldWidth;
    }

    public int getShieldHeight() {
        return shieldHeight;
    }

}
