package net.dohaw.blackclover.grimmoire.spell.type.cotton;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BlockSnapshot;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class LifeNet extends CastSpellWrapper implements Listener {

    private int duration;
    private int castDistance;

    public LifeNet(GrimmoireConfig grimmoireConfig) {
        super(SpellType.LIFE_NET, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Block targetBlock = player.getTargetBlockExact(castDistance);
        if(targetBlock != null){

            List<BlockSnapshot> snapshots = new ArrayList<>();
            Location targetBlockLocation = targetBlock.getLocation().clone();
            SpellUtils.spawnParticle(targetBlockLocation, Particle.END_ROD, 10, 0.5f, 0.5f, 0.5f);
            SpellUtils.playSound(targetBlockLocation, Sound.BLOCK_WOOL_PLACE);

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    Block woolBlock = targetBlockLocation.clone().add(i, 0, j).getBlock();
                    BlockSnapshot snapshot = BlockSnapshot.toSnapshot(woolBlock);
                    snapshots.add(snapshot);
                    woolBlock.setType(Material.WHITE_WOOL);
                    woolBlock.setMetadata("life-net-marker", new FixedMetadataValue(Grimmoire.instance, 1));
                }
            }
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
               snapshots.forEach(snap -> {
                   snap.apply();
                   snap.getLocation().getBlock().removeMetadata("life-net-marker", Grimmoire.instance);
               });
            }, duration * 20);
        }else{
            player.sendMessage("There is no block within distance you can target!");
            return false;
        }

        return true;
    }

    /**
     * Cancels damage when falling on the life net.
     */
    @EventHandler
    public void onTakeFallDamage(EntityDamageEvent e){
        if(e.getCause() == EntityDamageEvent.DamageCause.FALL){
            Entity entity = e.getEntity();
            Block blockFellOn = entity.getLocation().subtract(0, 1, 0).getBlock();
            if(blockFellOn.hasMetadata("life-net-marker")){
                SpellUtils.playSound(entity, Sound.BLOCK_WOOL_PLACE);
                SpellUtils.spawnParticle(entity, Particle.END_ROD, 10, 0.5f, 0.5f, 0.5f);
                e.setCancelled(true);
            }
        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.duration = grimmoireConfig.getIntegerSetting(KEY, "Duration");
    }
}
