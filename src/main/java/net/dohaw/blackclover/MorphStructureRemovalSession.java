package net.dohaw.blackclover;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class MorphStructureRemovalSession {

    private Location morphLocation;
    private TreeType treeType;

    private final BlockFace[] VALID_BLOCK_FACES = {BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH};
    private Material trunkMaterial, foliageMaterial;
    private int count;

    public MorphStructureRemovalSession(Location morphLocation, TreeType treeType){
        this.morphLocation = morphLocation;
        this.treeType = treeType;
    }

    /**
     * Destroys whatever they morphed in. This could be a red mushroom, oak tree, spruce tree, etc
     */
    private void findMorphStructureBlocks(Location location){

        final int MAX_BLOCK_CHANGES = 500;

        if(count < MAX_BLOCK_CHANGES){

            location.getBlock().setType(Material.AIR);

            List<Block> currentBlocksChanging = new ArrayList<>();
            for(BlockFace face : VALID_BLOCK_FACES){
                Block blockRelative = location.getBlock().getRelative(face);
                if(blockRelative.getType() == foliageMaterial || blockRelative.getType() == trunkMaterial){
                    currentBlocksChanging.add(blockRelative);
                    count++;
                    System.out.println("COUNT: " + count);
                }
            }

            for(Block b : currentBlocksChanging){
                findMorphStructureBlocks(b.getLocation());
            }

        }

    }

    private void setFoliageAndTrunkMaterials(){
        switch(treeType){
            case RED_MUSHROOM:
                this.foliageMaterial = Material.RED_MUSHROOM_BLOCK;
                this.trunkMaterial = Material.MUSHROOM_STEM;
            case BROWN_MUSHROOM:
                this.foliageMaterial = Material.BROWN_MUSHROOM_BLOCK;
                break;
            case TREE:
                this.foliageMaterial = Material.OAK_LEAVES;
                this.trunkMaterial = Material.OAK_LOG;
                break;
            case REDWOOD:
                this.foliageMaterial = Material.SPRUCE_LEAVES;
                this.trunkMaterial = Material.SPRUCE_LOG;
                break;
            case BIRCH:
                this.foliageMaterial = Material.BIRCH_LEAVES;
                this.trunkMaterial = Material.BIRCH_LOG;
                break;
            case JUNGLE:
                this.foliageMaterial = Material.JUNGLE_LEAVES;
                this.trunkMaterial = Material.JUNGLE_LOG;
                break;
        }
    }

    private void destroyCacti(){

    }

    public void startRemovalProcess(){
        setFoliageAndTrunkMaterials();
        if(treeType != TreeType.CHORUS_PLANT){
            long startTime = System.currentTimeMillis();
            findMorphStructureBlocks(morphLocation);
            long endTime = System.currentTimeMillis();
            System.out.println("Took " + (endTime - startTime) + " ns");
        }else{
            destroyCacti();
        }
    }


}
