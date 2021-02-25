package net.dohaw.blackclover.util;

import lombok.NonNull;
import net.dohaw.corelib.helpers.MathHelper;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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

    public static void spawnParticle(Block block, Particle particle, int count, float offsetX, float offsetY, float offsetZ){
        spawnParticle(block.getLocation(), particle, count, offsetX, offsetY, offsetZ);
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
