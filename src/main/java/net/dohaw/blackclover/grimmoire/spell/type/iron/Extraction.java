package net.dohaw.blackclover.grimmoire.spell.type.iron;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DependableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
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

public class Extraction extends CastSpellWrapper implements DependableSpell, Listener {

    public Extraction(GrimmoireConfig grimmoireConfig) {
        super(SpellType.EXTRACTION, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        Player p = pd.getPlayer();
        List<Location> locs = getSphere(p.getLocation(), 20, false);
        int counter = 0;
        for (Location loc : locs) {
            Block b = p.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            if (b.getType() == Material.IRON_ORE) {
                b.setType(Material.STONE);
                counter++;
            }
        }
        if (counter > 0) {
            for (int i = 0; i < counter/64; i++) {
                p.getInventory().addItem(new ItemStack(Material.IRON_ORE, 64));
            }
            p.getInventory().addItem(new ItemStack(Material.IRON_ORE, counter%64));
            p.sendMessage(((counter == 1)? "1 block has" : counter + " blocks have") + " been added to your inventory");
            for (int i = 0; i < (Math.min(counter, 3)); i++) {
                Bukkit.getServer().getScheduler().runTaskLater(Grimmoire.instance, () -> {
                    p.playSound(p.getLocation(), Sound.BLOCK_STONE_HIT, 10, 10);
                }, 5L * i);
            }
        } else {
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, 10, 10);
        }
        return false;
    }

    public static List<Location> getSphere(Location centerBlock, int radius, boolean hollow) {
        if (centerBlock == null) {
            return new ArrayList<>();
        }
        List<Location> circleBlocks = new ArrayList<Location>();
        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();
        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius/2; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {

                    double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));

                    if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location l = new Location(centerBlock.getWorld(), x, y, z);

                        circleBlocks.add(l);

                    }

                }
            }
        }
        return circleBlocks;
    }


    @Override
    public void prepareShutdown() {}
    @Override
    public void initDependableData() {}
}
