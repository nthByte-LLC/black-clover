package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.util.BlockSnapshot;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Snowman;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SnowPlayerData extends PlayerData{

    @Getter
    private List<BlockSnapshot> changedIceAgeBlocks = new ArrayList<>();

    @Getter
    private List<BlockSnapshot> blocksSkatedOver = new ArrayList<>();

    @Setter
    private Snowman snowman;

    @Getter @Setter
    private boolean skating;

    public SnowPlayerData(UUID uuid) {
        super(uuid, Grimmoire.SNOW);
    }

    public boolean isSnowmanSpawned(){
        return snowman != null;
    }

    public void removeSnowman(){
        snowman.remove();
        snowman = null;
    }

    public void stopSkating(){
        this.skating = false;
        for(BlockSnapshot snapshot : blocksSkatedOver){
            Block block = snapshot.getLocation().getBlock();
            block.setBlockData(snapshot.getData());
        }
        blocksSkatedOver.clear();
    }

    public void addBlockSkatedOver(Block block){
        blocksSkatedOver.add(BlockSnapshot.toSnapshot(block));
    }

    public boolean hasSkatedOverBlock(Block block){
        return blocksSkatedOver.contains(BlockSnapshot.toSnapshot(block));
    }

    public void addChangedIceAgeBlock(Block block){
        changedIceAgeBlocks.add(BlockSnapshot.toSnapshot(block));
    }

}
