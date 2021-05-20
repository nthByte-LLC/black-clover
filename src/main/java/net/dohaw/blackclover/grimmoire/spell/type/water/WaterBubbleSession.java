package net.dohaw.blackclover.grimmoire.spell.type.water;

import net.dohaw.blackclover.util.BlockSnapshot;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;

public class WaterBubbleSession {

    private List<BlockSnapshot> insideBubble;
    private List<Block> outline;

    public WaterBubbleSession(List<Block> outline, List<BlockSnapshot> insideBubble){
        this.outline = outline;
        this.insideBubble = insideBubble;
    }

    public void finish(){
        insideBubble.forEach(snapshot -> {
            // If the snapshot says that the block was previously water, then we set it back to water.
            if(snapshot.getData().getMaterial() == Material.WATER){
                snapshot.getLocation().getBlock().setType(Material.WATER);
            }
        });
    }

    public List<Block> getOutline() {
        return outline;
    }

}
