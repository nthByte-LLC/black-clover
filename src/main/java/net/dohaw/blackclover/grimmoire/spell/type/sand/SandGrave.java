package net.dohaw.blackclover.grimmoire.spell.type.sand;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DamageableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.ShapeUtils;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SandGrave extends CastSpellWrapper implements DamageableSpell, Listener {

    private final NamespacedKey NSK = NamespacedKey.minecraft("marked-sand-grave");

    private int castDistance, radius, blocksAbovePlayer, sandStayTime;

    public SandGrave(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SAND_GRAVE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityHit = SpellUtils.getEntityInLineOfSight(player, castDistance);
        if(entityHit != null){

            Location centerBlock = entityHit.getLocation().add(0, blocksAbovePlayer + radius, 0);
            List<Location> sphereLocations = ShapeUtils.generateSphere(centerBlock, radius, false);
            List<FallingBlock> sandBlocks = new ArrayList<>();
            sphereLocations.forEach(loc -> {
                FallingBlock fBlock = loc.getWorld().spawnFallingBlock(loc, Material.SAND.createBlockData());
                fBlock.setDropItem(false);
                fBlock.getPersistentDataContainer().set(NSK, PersistentDataType.STRING, "boo");
                sandBlocks.add(fBlock);
            });

            System.out.println("CASTING");

            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                for(FallingBlock block : sandBlocks){
                    block.getLocation().getBlock().setType(Material.AIR);
                    block.remove();
                }
            }, sandStayTime * 20);

            SpellUtils.playSound(entityHit, Sound.BLOCK_ANVIL_PLACE);
            SpellUtils.spawnParticle(entityHit, Particle.EXPLOSION_NORMAL, 30, 0.1f, 0.1f, 0.1f);
            SpellUtils.spawnParticle(player, Particle.SPELL, 30, 0.1f, 0.1f, 0.1f);
            return true;

        }
        return false;
    }

    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent e){
        if(e.getEntityType() == EntityType.FALLING_BLOCK){
            FallingBlock fBlock = (FallingBlock) e.getEntity();
            if(fBlock.getMaterial() == Material.SAND){
                if(fBlock.getPersistentDataContainer().has(NSK, PersistentDataType.STRING)){
                    System.out.println("HAS NSK");
                    fBlock.getLocation().getBlock().setType(Material.SAND);
                    e.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = (int) grimmoireConfig.getNumberSetting(KEY, "Cast Distance");
        this.blocksAbovePlayer = (int) grimmoireConfig.getNumberSetting(KEY, "Blocks Above Player");
        this.radius = (int) grimmoireConfig.getNumberSetting(KEY, "Radius Ball");
        this.sandStayTime = (int) grimmoireConfig.getNumberSetting(KEY, "Sand Stay Time");
    }

}
