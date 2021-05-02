package net.dohaw.blackclover.grimmoire.spell.type.iron;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DependableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BlockSnapshot;
import net.dohaw.blackclover.util.ShapeUtils;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;

public class Dome extends CastSpellWrapper implements Listener {

    private double duration;

    private ArrayList<Material> replaceableBlocks = new ArrayList<>(Collections.singletonList(Material.GRASS));
    private ArrayList<List<Location>> unbreakableLocs = new ArrayList<>();

    public Dome(GrimmoireConfig grimmoireConfig) {
        super(SpellType.DOME, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player p = pd.getPlayer();
        
        List<Location> sphereLocs = ShapeUtils.generateSphere(p.getLocation(), 4, true);
        List<BlockSnapshot> previousBlocks = new ArrayList<>();

        Location glowstoneLocation = sphereLocs.get(86);

        unbreakableLocs.add(sphereLocs);
        for (Location loc : sphereLocs) {

            Block b = loc.getBlock();
            if (b.getType() == Material.AIR || replaceableBlocks.contains(b.getType())) {

                previousBlocks.add(BlockSnapshot.toSnapshot(b));
                if(loc.equals(glowstoneLocation)){
                    b.setType(Material.GLOWSTONE);
                }else{
                    b.setType(Material.IRON_BLOCK);
                }

            }

        }

        SpellUtils.playSound(glowstoneLocation, Sound.BLOCK_ANVIL_PLACE);
        Bukkit.getServer().getScheduler().runTaskLater(Grimmoire.instance, () -> {
            for(BlockSnapshot snap : previousBlocks){
                snap.apply();
            }
            SpellUtils.playSound(glowstoneLocation, Sound.BLOCK_ANVIL_BREAK);
        }, (long) (20L * duration));

        return true;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        unbreakableLocs.forEach(locs -> {
            locs.forEach(loc ->{
                if (b.getLocation().equals(loc)) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("You can't break this!");
                }
            });
        });
    }

    @Override
    public void prepareShutdown() {}

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

}
