package net.dohaw.blackclover.util;

import lombok.NonNull;
import net.dohaw.corelib.helpers.MathHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

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
