package net.dohaw.blackclover.grimmoire.spell.type.glass;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.ProjectileSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

public class GlassShot extends ProjectileSpellWrapper {

    public GlassShot(GrimmoireConfig grimmoireConfig) {
        super(SpellType.GLASS_SHOT, grimmoireConfig);
    }

    @Override
    public void onProjectileLaunch(LivingEntity projectileLauncher) {
        SpellUtils.spawnParticle(projectileLauncher, Particle.CLOUD, 30, 1, 1, 1);
        SpellUtils.playSound(projectileLauncher, Sound.BLOCK_GLASS_STEP);
    }

    @Override
    public void onProjectileHit(LivingEntity entityHit) {
        SpellUtils.playSound(entityHit, Sound.BLOCK_GLASS_BREAK);
    }

    @Override
    public Material getProjectileMaterial() {
        return Material.GLASS_BOTTLE;
    }

    @Override
    public void prepareShutdown() {

    }

}
