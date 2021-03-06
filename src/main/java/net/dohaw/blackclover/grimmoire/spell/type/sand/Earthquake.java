package net.dohaw.blackclover.grimmoire.spell.type.sand;

import lombok.Getter;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.EarthquakeRunner;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Earthquake extends CastSpellWrapper {

    @Getter
    private double duration;

    @Getter
    private int radius;

    public Earthquake(GrimmoireConfig grimmoireConfig) {
        super(SpellType.EARTHQUAKE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        new EarthquakeRunner(player, this).runTaskTimer(Grimmoire.instance, 0L, 5L);
        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getNumberSetting(KEY, "Radius");
        this.duration = grimmoireConfig.getNumberSetting(KEY, "Duration");
    }
}
