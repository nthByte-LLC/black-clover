package net.dohaw.blackclover.runnable.spells.vortex;

import net.dohaw.blackclover.grimmoire.spell.type.vortex.Firado;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.ShapeUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FiradoRunner extends AbstractVortexTornadoRunner {

    private Firado spell;

    public FiradoRunner(Entity entity, Firado spell) {
        super(entity, spell, new Particle.DustOptions(Color.ORANGE, 1), 20L);
        this.spell = spell;
    }

    @Override
    public void doTornadoSpecifics() {
        burnNearbyEntities();
        burnNearbyTrees();
    }

    private void burnNearbyEntities(){

        Collection<Entity> entitiesNearTornado = getEntitiesNearTornado();
        for(Entity entityNearTornado : entitiesNearTornado){
            if(entityNearTornado instanceof LivingEntity){

                LivingEntity leNearTornado = (LivingEntity) entityNearTornado;

                int currentEntityFireTicks = leNearTornado.getFireTicks();
                int maxEntityFireTicks = leNearTornado.getMaxFireTicks();
                int newEntityFireTicks = currentEntityFireTicks + spell.getBurningTimeAdded();
                if(newEntityFireTicks > maxEntityFireTicks){
                    newEntityFireTicks = maxEntityFireTicks;
                }
                leNearTornado.setFireTicks(newEntityFireTicks);

            }
        }

    }

    private void burnNearbyTrees(){

        ThreadLocalRandom current = ThreadLocalRandom.current();
        List<BlockFace> checkedBlockFaces = Arrays.asList(BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.SOUTH, BlockFace.NORTH, BlockFace.WEST);

        TornadoParticleRunner firstTornado = tornadoes.get(0);
        Entity centeredArmorStand = firstTornado.getEntity();
        int currentCheckingRadius = 1;

        List<Location> particleLocations = firstTornado.getParticleLocations();
        Location lastParticleLocation = particleLocations.get(particleLocations.size() - 1);
        double tornadoHeight = lastParticleLocation.getY() - centeredArmorStand.getLocation().getY();
        if(tornadoHeight < 1){
            tornadoHeight = 1;
        }

        for(int y = 0; y < tornadoHeight; y++){

            Location loc = centeredArmorStand.getLocation().clone().add(0, y, 0);
            List<Block> heightLevelBlocks = ShapeUtils.getBlocksInCube(loc, currentCheckingRadius, 1, null);
            for(Block block : heightLevelBlocks){

                String blockTypeName = block.getType().toString().toLowerCase();
                if(blockTypeName.contains("log") || blockTypeName.contains("leaves")){
                    // Sets a random block face on fire
                    BlockFace randomBlockFace = checkedBlockFaces.get(current.nextInt(checkedBlockFaces.size()));
                    Block relativeBlock = block.getRelative(randomBlockFace);
                    relativeBlock.setType(Material.FIRE);
                }

            }
            currentCheckingRadius += 1;

        }

    }

}
