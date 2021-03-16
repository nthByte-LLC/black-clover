package net.dohaw.blackclover.grimmoire.spell.type.water;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BlockSnapshot;
import net.dohaw.blackclover.util.ShapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.*;

public class WaterBubble extends CastSpellWrapper implements Listener {

    private Map<UUID, List<Block>> outlineOfWaterBubbles = new HashMap<>();

    private double duration;
    private int radius;

    public WaterBubble(GrimmoireConfig grimmoireConfig) {
        super(SpellType.WATER_BUBBLE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        // Gets the outline of the cube.
        List<Block> cubeOutline = ShapeUtils.getHollowCube(player.getLocation(), radius);
        // Gets the blocks within the cube and sets them as air to create a air bubble
        // Gets the snapshot object so that we do a material check later down the road (In the runTaskLater runnable)
        List<BlockSnapshot> cubeBlocks = ShapeUtils.getBlockSnapshotsInCube(player.getLocation(), radius);
        cubeBlocks.forEach(snapshot -> snapshot.getLocation().getBlock().setType(Material.AIR));
        outlineOfWaterBubbles.put(player.getUniqueId(), cubeOutline);
        // Replaces the air that was set earlier back to water.
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
           outlineOfWaterBubbles.remove(player.getUniqueId());
           cubeBlocks.forEach(snapshot -> {
               // If the snapshot says that the block was previously water, then we set it back to water.
               if(snapshot.getData().getMaterial() == Material.WATER){
                   snapshot.getLocation().getBlock().setType(Material.WATER);
               }
           });
        }, (long) (duration * 20));
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getIntegerSetting(KEY, "Radius");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

    @EventHandler
    public void onBlockChange(BlockFromToEvent e){
        for(List<Block> outline : outlineOfWaterBubbles.values()){
            if(outline.contains(e.getBlock()) || outline.contains(e.getToBlock())){
                e.setCancelled(true);
            }
        }
    }

}
