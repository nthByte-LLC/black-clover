package net.dohaw.blackclover.grimmoire.spell.type.fire;

import lombok.Getter;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.FireStormRunner;
import org.bukkit.event.Event;

public class FireStorm extends CastSpellWrapper {

    @Getter
    private double radiusParticles;

    @Getter
    private double fireballFrequency;

    @Getter
    private int numFireballWaves;

    @Getter
    private int numFireBalls;

    public FireStorm(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_STORM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        new FireStormRunner(pd.getPlayer(), this).runTaskTimer(Grimmoire.instance, 0L, (long) (fireballFrequency * 20));
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.numFireBalls = grimmoireConfig.getIntegerSetting(KEY, "Number Fireballs Spawned");
        this.numFireballWaves = grimmoireConfig.getIntegerSetting(KEY, "Number Fireball Waves");
        this.fireballFrequency = grimmoireConfig.getDoubleSetting(KEY, "Fireball Rate");
        this.radiusParticles = grimmoireConfig.getDoubleSetting(KEY, "Particle Radius");
    }

    @Override
    public void prepareShutdown() {

    }

}
