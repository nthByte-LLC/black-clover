package net.dohaw.blackclover.util;

import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.corelib.helpers.MathHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftSnowball;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpellUtils {

    public static void playSound(Location location, Sound sound){
        location.getWorld().playSound(location, sound, (float) 0.5, 1);
    }

    public static void playSound(Entity entity, Sound sound){
        playSound(entity.getLocation(), sound);
    }

    public static void playSound(Block block, Sound sound){
        playSound(block.getLocation(), sound);
    }

    public static void spawnParticle(Location location, Particle particle, int count, float offsetX, float offsetY, float offsetZ){
        Objects.requireNonNull(location.getWorld()).spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
    }

    public static void spawnParticle(Entity entity, Particle particle, int count, float offsetX, float offsetY, float offsetZ){
        spawnParticle(entity.getLocation(), particle, count, offsetX, offsetY, offsetZ);
    }

    public static void spawnParticle(Entity entity, Particle particle, BlockData blockData, int count, float offsetX, float offsetY, float offsetZ){
        Objects.requireNonNull(entity.getWorld()).spawnParticle(particle, entity.getLocation(), count, offsetX, offsetY, offsetZ, blockData);
    }

    public static void spawnParticle(Location loc, Particle particle, Particle.DustOptions data, int count, float offsetX, float offsetY, float offsetZ){
        Objects.requireNonNull(loc.getWorld()).spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, data);
    }

    public static void spawnParticle(Block block, Particle particle, int count, float offsetX, float offsetY, float offsetZ){
        spawnParticle(block.getLocation(), particle, count, offsetX, offsetY, offsetZ);
    }

    public static Entity getEntityInLineOfSight(Player player, int distance){
        Location start = player.getLocation();
        Vector dir = start.getDirection();
        for (double i = 0; i < distance; i += 0.5) {
            Vector currentDir = dir.clone().multiply(i);
            Location currentLocation = start.clone().add(currentDir);
            List<Entity> nearbyEntities = new ArrayList<>(player.getWorld().getNearbyEntities(currentLocation, 1, 1, 1, (e) -> e instanceof LivingEntity));
            nearbyEntities.removeIf(e -> e.getUniqueId().equals(player.getUniqueId()));
            if(!nearbyEntities.isEmpty()){
                return nearbyEntities.get(0);
            }
        }
        return null;
    }

    public static Projectile fireProjectile(Player player, CastSpellWrapper wrapper, Material projMaterial){
        CraftLivingEntity cPlayer = (CraftLivingEntity) player;
        Projectile projectile = cPlayer.launchProjectile(Snowball.class);
        wrapper.markAsSpellBinding(projectile);
        ((CraftSnowball) projectile).getHandle().setItem(CraftItemStack.asNMSCopy(new ItemStack(Material.FIRE_CHARGE)));
        return projectile;
    }

    public static double getRandomDamageModifier(){
        int randomNum = MathHelper.getRandomInteger(1, 0);
        //Additive
        if(randomNum == 0){
            return MathHelper.getRandomInteger(5, 1);
        }else{
            return MathHelper.getRandomInteger(5, 1) * -1;
        }
    }

}
