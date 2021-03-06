package net.dohaw.blackclover.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

@AllArgsConstructor
public class BlockSnapshot {

    @Getter
    private BlockData data;

    @Getter
    private Location location;

    public static BlockSnapshot toSnapshot(Block block){
        return new BlockSnapshot(block.getBlockData().clone(), block.getLocation().clone());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockSnapshot snapshot = (BlockSnapshot) o;
        return data.equals(snapshot.data) &&
                location.equals(snapshot.location);
    }

}
