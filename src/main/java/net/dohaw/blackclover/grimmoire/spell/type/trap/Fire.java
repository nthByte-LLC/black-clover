package net.dohaw.blackclover.grimmoire.spell.type.trap;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Fire extends TrapSpell{

    private double damage;

    public Fire(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE, grimmoireConfig);
    }

    @Override
    public void onStepOnTrap(Trap trap, LivingEntity trapStepper) {
        // Owner can't be null because in order for the trap to be on the ground, the owner of the trap has to be online
        Player owner = Bukkit.getPlayer(trap.getOwner());
        boolean didDamage = SpellUtils.doSpellDamage(trapStepper, owner, KEY, damage);
        if(didDamage){
            SpellUtils.playWorldEffect(trapStepper, Effect.MOBSPAWNER_FLAMES);
            SpellUtils.playSound(trapStepper, Sound.ITEM_FIRECHARGE_USE);
        }
    }

    @Override
    public Material getCarpetMaterial() {
        return Material.RED_CARPET;
    }

    @Override
    public Particle placeParticles() {
        return Particle.FLAME;
    }

    @Override
    public TrapType getTrapType() {
        return TrapType.FIRE;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
    }

}
