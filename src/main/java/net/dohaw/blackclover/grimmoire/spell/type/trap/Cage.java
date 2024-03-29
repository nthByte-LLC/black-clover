package net.dohaw.blackclover.grimmoire.spell.type.trap;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.BlockSnapshot;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cage extends TrapSpell{

    public Cage(GrimmoireConfig grimmoireConfig) {
        super(SpellType.CAGE, grimmoireConfig);
    }

    @Override
    public void onStepOnTrap(Trap trap, LivingEntity trapStepper) {

        final List<BlockFace> CHECKED_FACES = Arrays.asList(BlockFace.NORTH, BlockFace.WEST, BlockFace.EAST, BlockFace.SOUTH);
        Block trapStepperBlock = trapStepper.getLocation().getBlock();
        List<BlockSnapshot> previousBlocks = new ArrayList<>();

        for(BlockFace face : CHECKED_FACES){

            Block relative = trapStepperBlock.getRelative(face).getRelative(BlockFace.UP);
            previousBlocks.add(BlockSnapshot.toSnapshot(relative));
            relative.setType(Material.IRON_DOOR);

            Door door = (Door) relative.getBlockData();
            door.setFacing(face);
            door.setHalf(Bisected.Half.TOP);
            relative.setBlockData(door);

            /*
                Under
             */
            Block relativeUnder = relative.getRelative(BlockFace.DOWN);
            previousBlocks.add(BlockSnapshot.toSnapshot(relativeUnder));
            relativeUnder.setType(Material.IRON_DOOR);

            Door doorBottom = (Door) relativeUnder.getBlockData();
            doorBottom.setFacing(face);
            door.setHalf(Bisected.Half.BOTTOM);
            relativeUnder.setBlockData(doorBottom);

        }

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

    @Override
    public void prepareShutdown() {
        super.prepareShutdown();
    }
    
}
