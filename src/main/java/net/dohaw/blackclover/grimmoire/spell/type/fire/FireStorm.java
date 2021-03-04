package net.dohaw.blackclover.grimmoire.spell.type.fire;

import lombok.Getter;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.Projectable;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.FireStormRunner;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireStorm extends CastSpellWrapper implements Projectable {

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
        deductMana(pd);
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.numFireBalls = grimmoireConfig.getNumberSetting(KEY, "Number Fireballs Spawned");
        this.numFireballWaves = grimmoireConfig.getNumberSetting(KEY, "Number Fireball Waves");
        this.fireballFrequency = grimmoireConfig.getNumberSetting(KEY, "Fireball Rate");
        this.radiusParticles = grimmoireConfig.getNumberSetting(KEY, "Particle Radius");
    }

    @Override
    public void onHit(EntityDamageByEntityEvent event, Entity eDamaged, PlayerData pdDamager) {
        System.out.println("HITTING");
    }

}
