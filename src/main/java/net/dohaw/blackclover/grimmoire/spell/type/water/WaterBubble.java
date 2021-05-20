package net.dohaw.blackclover.grimmoire.spell.type.water;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BlockSnapshot;
import net.dohaw.blackclover.util.ShapeUtils;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.*;

public class WaterBubble extends ActivatableSpellWrapper implements Listener {

    private Map<UUID, WaterBubbleSession> waterBubbleSessions = new HashMap<>();

    private int radius;

    public WaterBubble(GrimmoireConfig grimmoireConfig) {
        super(SpellType.WATER_BUBBLE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();
        // Gets the outline of the cube.
        List<Block> cubeOutline = ShapeUtils.getHollowCube(player.getLocation(), radius);
        // Gets the blocks within the cube and sets them as air to create a air bubble
        List<BlockSnapshot> cubeBlocks = ShapeUtils.getBlockSnapshotsInCube(player.getLocation(), radius);
        cubeBlocks.removeIf(snapshot -> snapshot.getData().getMaterial() != Material.WATER);
        cubeBlocks.forEach(snapshot -> {
            snapshot.getLocation().getBlock().setType(Material.AIR);
        });

        WaterBubbleSession session = new WaterBubbleSession(cubeOutline, cubeBlocks);
        waterBubbleSessions.put(player.getUniqueId(), session);

        return super.cast(e, pd);
    }

    @Override
    public void doRunnableTick(PlayerData caster) {
        SpellUtils.playSound(caster.getPlayer(), Sound.ENTITY_BOAT_PADDLE_WATER);
    }

    @Override
    public void deactiveSpell(PlayerData caster) {
        WaterBubbleSession session = waterBubbleSessions.get(caster.getUUID());
        if(session != null){
            session.finish();
            waterBubbleSessions.remove(caster.getUUID());
        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getIntegerSetting(KEY, "Radius");
    }

    @EventHandler
    public void onBlockChange(BlockFromToEvent e){
        for(WaterBubbleSession session : waterBubbleSessions.values()){
            List<Block> outline = session.getOutline();
            if(outline.contains(e.getBlock()) || outline.contains(e.getToBlock())){
                e.setCancelled(true);
            }
        }
    }

}
