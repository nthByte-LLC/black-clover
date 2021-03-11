package net.dohaw.blackclover.grimmoire.spell.type.shakudo;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BlockUtil;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.ResponderFactory;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GhostWolf extends CastSpellWrapper implements Listener {

    private final NamespacedKey NSK = NamespacedKey.minecraft("ghost-wolf");

    private int biteDamage;
    private int castDistance;

    public GhostWolf(GrimmoireConfig grimmoireConfig) {
        super(SpellType.GHOST_WOLF, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(player, castDistance);
        ResponderFactory rf = new ResponderFactory(player);
        if(entityInSight != null){
            System.out.println("ENTITY: " + entityInSight);
            if(entityInSight instanceof LivingEntity) {

                LivingEntity livingEntityInSight = (LivingEntity) entityInSight;
                Wolf wolf = (Wolf) player.getWorld().spawnEntity(BlockUtil.getBlockInFront(player, 1).add(0, 1, 0), EntityType.WOLF);
                wolf.setInvisible(true);
                wolf.setGlowing(true);
                wolf.setTarget(livingEntityInSight);
                // Marks the wolf as a ghost wolf
                PersistentDataContainer pdc = wolf.getPersistentDataContainer();
                pdc.set(NSK, PersistentDataType.STRING, "marker");

                SpellUtils.spawnParticle(wolf, Particle.END_ROD, 30, 1, 1, 1);
                SpellUtils.playSound(wolf, Sound.ENTITY_WOLF_HOWL);

                return true;
            }else{
                rf.sendMessage("This is not a valid entity!");
            }
        }else{
            rf.sendMessage("No entity has been found at a reasonable distance!");
        }

        return false;
    }

    @EventHandler
    public void onGhostWolfDamage(EntityDamageByEntityEvent e){

        Entity eDamager = e.getDamager();
        if(eDamager instanceof Wolf && eDamager.getPersistentDataContainer().has(NSK, PersistentDataType.STRING)){
            e.setDamage(biteDamage);
            eDamager.remove();
            SpellUtils.spawnParticle(eDamager, Particle.SQUID_INK, 30, 1, 1, 1);
            SpellUtils.playSound(eDamager, Sound.BLOCK_BEACON_DEACTIVATE);
        }

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = (int) grimmoireConfig.getNumberSetting(KEY, "Cast Distance");
        this.biteDamage = (int) grimmoireConfig.getNumberSetting(KEY, "Bite Damage");
    }
}
