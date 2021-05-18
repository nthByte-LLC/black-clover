package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class EndGate extends MCPortalCreationSpell{

    public EndGate(GrimmoireConfig grimmoireConfig) {
        super(SpellType.END_GATE, grimmoireConfig);
    }

    @Override
    public List<List<Block>> makePortal(Location startLocation) {

        List<List<Block>> portalBlocks = new ArrayList<>();

        portalBlocks.add(placeHorizontalBlocks(startLocation));
        portalBlocks.add(placeVerticalBlocks(startLocation));
        portalBlocks.add(placeHorizontalBlocks(LocationUtil.getAbsoluteLocationToRight(LocationUtil.getAbsoluteLocationInFront(startLocation, verticalLength - 1), 1)));
        portalBlocks.add(placeVerticalBlocks(LocationUtil.getAbsoluteLocationToRight(startLocation, horizontalLength)));

        return portalBlocks;
    }

    @Override
    public List<Block> makePortalEntrance(Location bottomLeftCorner) {

        List<Block> portalEntranceBlocks = new ArrayList<>();

        Location currentLocation;
        for(int x = 1; x < 4; x++){
            for(int y = 1; y < 4; y++){

                currentLocation = LocationUtil.getAbsoluteLocationInFront(bottomLeftCorner, y);
                currentLocation = LocationUtil.getAbsoluteLocationToRight(currentLocation, x);

                Block currentBlock = currentLocation.getBlock();
                currentBlock.setType(Material.END_PORTAL);
                portalEntranceBlocks.add(currentBlock);

            }
        }

        return portalEntranceBlocks;

    }

    @Override
    public Sound getPortalCreationSound() {
        return Sound.BLOCK_END_GATEWAY_SPAWN;
    }

}
