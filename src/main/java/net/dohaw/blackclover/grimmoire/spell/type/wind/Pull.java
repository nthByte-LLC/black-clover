package net.dohaw.blackclover.grimmoire.spell.type.wind;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.PullRunner;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Pull extends CastSpellWrapper {

    private double forceMultiplier, radius;
    private int numPulls;

    public Pull(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PULL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        new PullRunner(player, this).runTaskTimer(Grimmoire.instance, 0L, 20L);
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.forceMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Force Multiplier");
        this.numPulls = grimmoireConfig.getIntegerSetting(KEY, "Number of Pulls");
        this.radius = grimmoireConfig.getDoubleSetting(KEY, "Radius");
    }

    public int getNumPulls() {
        return numPulls;
    }

    public double getForceMultiplier() {
        return forceMultiplier;
    }

    public double getRadius() {
        return radius;
    }

}
