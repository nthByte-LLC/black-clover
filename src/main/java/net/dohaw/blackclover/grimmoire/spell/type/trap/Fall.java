package net.dohaw.blackclover.grimmoire.spell.type.trap;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

/**
 * Trap that makes the entity fall whenever they step on it
 */
public class Fall extends TrapSpell{

    private int heightFall;

    public Fall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FALL, grimmoireConfig);
    }

    @Override
    public void onStepOnTrap(Trap trap, LivingEntity trapStepper) {

        Location trapStepperLocation = trapStepper.getLocation();
        for(int i = 0; i < heightFall; i ++){
            Location blockLocation = trapStepperLocation.clone().subtract(0, i, 0);
            blockLocation.getBlock().setType(Material.AIR);
        }

        SpellUtils.playSound(trapStepper, Sound.BLOCK_WOODEN_TRAPDOOR_OPEN);

    }

    @Override
    public Material getCarpetMaterial() {
        return Material.GREEN_CARPET;
    }

    @Override
    public Particle placeParticles() {
        return Particle.CURRENT_DOWN;
    }

    @Override
    public TrapType getTrapType() {
        return TrapType.FALL;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.heightFall = grimmoireConfig.getIntegerSetting(KEY, "Height of Fall");
    }

}
