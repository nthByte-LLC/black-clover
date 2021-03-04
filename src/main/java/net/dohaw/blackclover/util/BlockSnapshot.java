package net.dohaw.blackclover.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

@AllArgsConstructor
public class BlockSnapshot {

    @Getter
    private BlockData data;

    @Getter
    private Location location;

}
