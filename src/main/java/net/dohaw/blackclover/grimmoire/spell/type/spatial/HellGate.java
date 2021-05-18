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

public class HellGate extends MCPortalCreationSpell {

    public HellGate(GrimmoireConfig grimmoireConfig) {
        super(SpellType.HELL_GATE, grimmoireConfig);
    }

    @Override
    public List<List<Block>> makePortal(Location startLocation) {

        List<List<Block>> portalBlocks = new ArrayList<>();

        portalBlocks.add(placeHorizontalBlocks(startLocation));
        portalBlocks.add(placeVerticalBlocks(startLocation));
        portalBlocks.add(placeHorizontalBlocks(startLocation.clone().add(0, verticalLength - 1, 0)));
        portalBlocks.add(placeVerticalBlocks(LocationUtil.getAbsoluteLocationToRight(startLocation, horizontalLength)));

        return portalBlocks;
    }

    @Override
    public List<Block> makePortalEntrance(Location startLocation) {
        Location fireLocation = LocationUtil.getAbsoluteLocationToRight(startLocation, 1).add(0, 1, 0);
        fireLocation.getBlock().setType(Material.FIRE);
        return null;
    }

    @Override
    public Sound getPortalCreationSound() {
        return Sound.BLOCK_PORTAL_TRIGGER;
    }

}
