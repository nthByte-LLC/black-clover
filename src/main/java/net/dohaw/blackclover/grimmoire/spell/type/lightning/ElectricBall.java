package net.dohaw.blackclover.grimmoire.spell.type.lightning;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.ParticleProjectileSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.BukkitColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.Listener;

/**
 * Shoots a electric ball from the player, sort of like a fireball.
 */
public class ElectricBall extends ParticleProjectileSpellWrapper {

    public ElectricBall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ELECTRIC_BALL, grimmoireConfig);
    }

    @Override
    public Particle getOnHitParticle() {
        return Particle.SOUL_FIRE_FLAME;
    }

    @Override
    public Sound getOnHitSound() {
        return Sound.BLOCK_FIRE_EXTINGUISH;
    }

    @Override
    public Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(BukkitColor.PALE_CYAN, 2);
    }

}
