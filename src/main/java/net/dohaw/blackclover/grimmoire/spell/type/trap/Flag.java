package net.dohaw.blackclover.grimmoire.spell.type.trap;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

public class Flag extends TrapSpell{

    private double durationGlowing;

    public Flag(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FLAG, grimmoireConfig);
    }

    @Override
    public void onStepOnTrap(Trap trap, LivingEntity trapStepper) {
        trapStepper.setGlowing(true);
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            trapStepper.setGlowing(false);
        }, (long) (durationGlowing * 20));
        SpellUtils.playSound(trapStepper, Sound.BLOCK_BELL_USE);
        SpellUtils.spawnParticle(trapStepper, Particle.FLASH, 30, 1, 1, 1);
    }

    @Override
    public Material getCarpetMaterial() {
        return Material.YELLOW_CARPET;
    }

    @Override
    public Particle placeParticles() {
        return Particle.FALLING_HONEY;
    }

    @Override
    public TrapType getTrapType() {
        return TrapType.FLAG;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.durationGlowing = grimmoireConfig.getDoubleSetting(KEY, "Duration Glowing");
    }

}
