package net.dohaw.blackclover.grimmoire.spell.type.iron;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Extraction extends CastSpellWrapper implements Listener {

    public Extraction(GrimmoireConfig grimmoireConfig) {
        super(SpellType.EXTRACTION, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player p = pd.getPlayer();
        List<Location> oreLocations = getIronLocations(p.getLocation(), 20, false);
        int numOres = oreLocations.size();
        if (numOres != 0) {

            for (int i = 0; i < numOres/64; i++) {
                p.getInventory().addItem(new ItemStack(Material.IRON_ORE, 64));
            }
            p.getInventory().addItem(new ItemStack(Material.IRON_ORE, numOres%64));
            p.sendMessage(((numOres == 1)? "1 block has" : numOres + " blocks have") + " been added to your inventory");
            SpellUtils.playSound(p, Sound.BLOCK_STONE_BREAK);

        } else {
            SpellUtils.playSound(p, Sound.BLOCK_ANVIL_HIT);
        }
        return false;
    }

    public static List<Location> getIronLocations(Location centerBlock, int radius, boolean hollow) {
        if (centerBlock == null) {
            return new ArrayList<>();
        }
        List<Location> ironLocation = new ArrayList<Location>();
        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();
        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius/2; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {

                    double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));

                    if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location loc = new Location(centerBlock.getWorld(), x, y, z);
                        Block b = loc.getBlock();
                        if(b.getType() == Material.IRON_ORE) ironLocation.add(loc);

                    }

                }
            }
        }
        return ironLocation;
    }

    @Override
    public void prepareShutdown() {}

}
