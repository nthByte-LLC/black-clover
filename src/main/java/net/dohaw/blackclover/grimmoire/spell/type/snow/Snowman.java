package net.dohaw.blackclover.grimmoire.spell.type.snow;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.SnowPlayerData;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Objects;
import java.util.UUID;

public class Snowman extends CastSpellWrapper implements Listener {

    private NamespacedKey OWNER_NSK = NamespacedKey.minecraft("snowman-owner");

    public Snowman(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SNOW_MAN, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        if(pd instanceof SnowPlayerData){
            SnowPlayerData spd = (SnowPlayerData) pd;
            if(!spd.isSnowmanSpawned()){

                Location snowmanLocation = LocationUtil.getLocationInFront(player, 1).add(0, 1, 0);
                org.bukkit.entity.Snowman snowman = (org.bukkit.entity.Snowman) player.getWorld().spawnEntity(snowmanLocation, EntityType.SNOWMAN);

                SpellUtils.spawnParticle(snowmanLocation, Particle.SNOWBALL, 30, 0.5f, 0.5f, 0.5f);
                SpellUtils.playSound(snowmanLocation, Sound.ITEM_CHORUS_FRUIT_TELEPORT);

                PersistentDataContainer pdc = snowman.getPersistentDataContainer();
                pdc.set(OWNER_NSK, PersistentDataType.STRING, player.getUniqueId().toString());
                spd.setSnowman(snowman);

            }else{
                if(player.isSneaking()){
                    spd.removeSnowman();
                    SpellUtils.playSound(player, Sound.BLOCK_SNOW_BREAK);
                }else{
                    player.sendMessage("You already have a snowman spawned!");
                    return false;
                }
            }
        }else{
            try{
                throw new UnexpectedPlayerData();
            } catch (UnexpectedPlayerData unexpectedPlayerData) {
                unexpectedPlayerData.printStackTrace();
            }
            return false;
        }

        return true;
    }

    // If the snowman is trying to target their owner, then it won't let them target them.
    @EventHandler
    public void onTargetOwner(EntityTargetEvent e){
        Entity entity = e.getEntity();
        if(entity.getType() == EntityType.SNOWMAN){
            System.out.println("SNOWMAN IS TARGETTING!");
            if(entity.getPersistentDataContainer().has(OWNER_NSK, PersistentDataType.STRING)){
                Entity target = e.getTarget();
                if(target != null){
                    if(target instanceof Player){
                        UUID snowmanOwner = UUID.fromString(Objects.requireNonNull(entity.getPersistentDataContainer().get(OWNER_NSK, PersistentDataType.STRING)));
                        if(target.getUniqueId().equals(snowmanOwner)){
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHitSnowman(EntityDamageByEntityEvent e){

        Entity damaged = e.getEntity();
        if(damaged.getType() == EntityType.SNOWMAN && damaged.getPersistentDataContainer().has(OWNER_NSK, PersistentDataType.STRING)){

            UUID snowmanOwner = UUID.fromString(Objects.requireNonNull(damaged.getPersistentDataContainer().get(OWNER_NSK, PersistentDataType.STRING)));
            Entity eDamager = e.getDamager();
            Player damager = null;

            if(eDamager instanceof Player){
                damager = (Player) eDamager;
            }else if(eDamager instanceof Projectile) {
                Projectile proj = (Projectile) eDamager;
                ProjectileSource shooter = proj.getShooter();
                if (shooter instanceof Player) {
                    damager = (Player) shooter;
                }
            }

            if(damager != null){
                // cancels the damager if you are the snowman's owner.
                if(damager.getUniqueId().equals(snowmanOwner)){
                    e.setCancelled(true);
                }
            }

        }
    }

}
