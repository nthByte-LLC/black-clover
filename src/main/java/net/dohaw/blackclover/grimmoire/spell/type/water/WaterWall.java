package net.dohaw.blackclover.grimmoire.spell.type.water;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.WaterWallRotator;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

/**
 * Repeatedly creates a water wall in the direction that the player is looking. Very similar to the Shield spell in the Glass Magic grimmoire.
 */
public class WaterWall extends CastSpellWrapper implements Listener {

    private double duration;

    public WaterWall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.WATER_WALL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        WaterWallRotator waterWallRotator = new WaterWallRotator(pd.getPlayer());
        waterWallRotator.runTaskTimer(Grimmoire.instance, 0L, 10L);
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, waterWallRotator::cancel, (long) (duration * 20));
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

}
