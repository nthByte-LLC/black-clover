package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.ParticleProjectileSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;

/**
 * Shoots a fire ball
 */
public class FireBall extends ParticleProjectileSpellWrapper {

    public FireBall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_BALL, grimmoireConfig);
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public Particle getOnHitParticle() {
        return Particle.FLAME;
    }

    @Override
    public Sound getOnHitSound() {
        return Sound.BLOCK_FIRE_EXTINGUISH;
    }

    @Override
    public Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.ORANGE, 2);
    }

}
