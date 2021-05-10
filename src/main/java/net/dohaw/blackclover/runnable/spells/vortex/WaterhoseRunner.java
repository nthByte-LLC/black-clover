package net.dohaw.blackclover.runnable.spells.vortex;

import net.dohaw.blackclover.grimmoire.spell.type.vortex.Waterhose;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.ShapeUtils;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.List;

public class WaterhoseRunner extends VortexTornadoRunner {

    public WaterhoseRunner(Entity entity, Waterhose spell) {
        super(entity, new Particle.DustOptions(Color.BLUE, 1), spell.getTornadoMaxTravelDistance(), 20L);
    }

    @Override
    public void doTornadoSpecifics() {
        extinguishNearbyTreesOnFire();
    }

    private void extinguishNearbyTreesOnFire(){

        TornadoParticleRunner firstTornado = tornadoes.get(0);
        Entity centeredArmorStand = firstTornado.getEntity();
        int currentCheckingRadius = 1;

        List<Location> particleLocations = firstTornado.getParticleLocations();
        Location lastParticleLocation = particleLocations.get(particleLocations.size() - 1);
        double tornadoHeight = lastParticleLocation.getY() - centeredArmorStand.getLocation().getY();
        if(tornadoHeight < 1){
            tornadoHeight = 1;
        }

        // set all the fire blocks to water.
        for(int y = 0; y < tornadoHeight; y++){

            Location loc = centeredArmorStand.getLocation().clone().add(0, y, 0);
            List<Block> heightLevelBlocks = ShapeUtils.getBlocksInCube(loc, currentCheckingRadius, 1, Material.FIRE);
            for(Block block : heightLevelBlocks){
                block.setType(Material.AIR);
                SpellUtils.playSound(block, Sound.BLOCK_FIRE_EXTINGUISH);
            }
            currentCheckingRadius += 1;

        }

    }

}
