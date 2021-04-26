package net.dohaw.blackclover.grimmoire.spell.type.lightning;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Collection;

public class LightningBolt extends CastSpellWrapper {

    private double damage;
    private int numParticlePoints;

    public LightningBolt(GrimmoireConfig grimmoireConfig) {
        super(SpellType.LIGHTNING_BOLT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Location boltStart = LocationUtil.getLocationToRight(player.getEyeLocation().subtract(0, 0.5, 0), 0.5);
        boolean isGoingLeft = true;
        int directionCount = 0;
        int count = 0;
        Location currentBoltLocation = boltStart.clone();

        while(count <= numParticlePoints){

            currentBoltLocation = LocationUtil.getLocationInFront(currentBoltLocation, 0.4);
            if(directionCount == 3){
                directionCount = 0;
                isGoingLeft = !isGoingLeft;
            }

            if(isGoingLeft){
                currentBoltLocation = LocationUtil.getLocationToLeft(currentBoltLocation, 0.4);
            }else{
                currentBoltLocation = LocationUtil.getLocationToRight(currentBoltLocation, 0.4);
            }

            Material type = currentBoltLocation.getBlock().getType();
            if(type.isSolid()){
                break;
            }

            // 0.4 is just some random thing i came up with
            damageNearbyPlayers(player, currentBoltLocation.getWorld().getNearbyEntities(currentBoltLocation, 0.4, 0.4, 0.4));

            SpellUtils.spawnParticle(currentBoltLocation, Particle.REDSTONE, new Particle.DustOptions(BukkitColor.CYAN, 1), 30, 0, 0, 0);
            directionCount++;
            count++;

        }

        SpellUtils.playSound(player, Sound.ENTITY_LIGHTNING_BOLT_IMPACT);
        SpellUtils.spawnParticle(currentBoltLocation, Particle.FLASH, 30, 1, 1, 1);
        return true;
    }

    private void damageNearbyPlayers(Player player, Collection<Entity> nearbyEntities){
        for(Entity e : nearbyEntities){
            if(e instanceof LivingEntity && !e.getUniqueId().equals(player.getUniqueId())){
                LivingEntity le = (LivingEntity) e;
                SpellUtils.doSpellDamage(le, player, KEY, damage);
            }
        }
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.numParticlePoints = grimmoireConfig.getIntegerSetting(KEY, "Number of Particle Points");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
    }
}
