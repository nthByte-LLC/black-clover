package net.dohaw.blackclover.grimmoire.spell.type.trap;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class Cage extends TrapSpell{

    private double duration;

    public Cage(GrimmoireConfig grimmoireConfig) {
        super(SpellType.CAGE, grimmoireConfig);
    }

    @Override
    public void onStepOnTrap(Trap trap, LivingEntity trapStepper) {

        Location trapStepperLocation = trapStepper.getLocation();
        List<Location> doorLocations = new ArrayList<>();

        doorLocations.add(LocationUtil.getAbsoluteLocationInFront(trapStepperLocation, 1));
        doorLocations.add(LocationUtil.getAbsoluteLocationToLeft(trapStepperLocation, 1));
        doorLocations.add(LocationUtil.getAbsoluteLocationToRight(trapStepperLocation, 1));
        doorLocations.add(LocationUtil.getAbsoluteLocationInBack(trapStepperLocation, 1));

        doorLocations.forEach(loc -> loc.getBlock().setType(Material.IRON_DOOR));

    }

    @Override
    public Material getCarpetMaterial() {
        return Material.WHITE_CARPET;
    }

    @Override
    public Particle placeParticles() {
        return Particle.END_ROD;
    }

    @Override
    public TrapType getTrapType() {
        return TrapType.CAGE;
    }

}
