package net.dohaw.blackclover.grimmoire.spell.type.trap;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

public class Stun extends TrapSpell {

    private double freezeDuration;

    public Stun(GrimmoireConfig grimmoireConfig) {
        super(SpellType.STUN, grimmoireConfig);
    }

    @Override
    public void onStepOnTrap(Trap trap, LivingEntity le) {
        SpellUtils.freezeEntity(le, freezeDuration);
        SpellUtils.spawnParticle(le, Particle.PORTAL, 30, 1, 1, 1);
        SpellUtils.playSound(le, Sound.ENTITY_SLIME_JUMP);
    }

    @Override
    public Material getCarpetMaterial() {
        return Material.BLACK_CARPET;
    }

    @Override
    public Particle placeParticles() {
        return Particle.FIREWORKS_SPARK;
    }

    @Override
    public TrapType getTrapType() {
        return TrapType.STUN;
    }

    @Override
    public void prepareShutdown() {
        
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.freezeDuration = grimmoireConfig.getDoubleSetting(KEY, "Freeze Duration");
    }

}
