package net.dohaw.blackclover.util;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.corelib.helpers.MathHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
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

    public static void startTornadoParticles(Entity entity, Particle.DustOptions dustOptions, boolean yIncrease, double radius, boolean goesRight){
        new TornadoParticleRunner(entity, dustOptions, yIncrease, radius, goesRight).runTaskTimer(Grimmoire.instance, 0L, 3L);
    }

    public static void startDoubleTornadoParticles(Entity entity, Particle.DustOptions data1, Particle.DustOptions data2, boolean yIncrease, double radius){
        startTornadoParticles(entity, data1, yIncrease, radius, true);
        startTornadoParticles(entity, data2, yIncrease, radius, false);
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

    public static double alterHealth(LivingEntity livingEntity, double amount){
        double health = livingEntity.getHealth();
        double maxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double newHealth = health + amount;
        if(newHealth > 20) {
            livingEntity.setHealth(maxHealth);
        }else if(newHealth <= 0){
            livingEntity.setHealth(0);
        }else{
            livingEntity.setHealth(newHealth);
        }
        // changed health amount
        return (health + Math.abs(amount)) - health;
    }

    /*
        Valid meaning the target isn't null and it's an instance of LivingEntity
     */
    //TODO: Need to refractor code to where they're all using this method instead of hardcoding this every single time.
    public static boolean isTargetValid(Player player, Entity entityInSight){
        if(entityInSight != null){
            if(entityInSight instanceof LivingEntity){
                return true;
            }else{
                player.sendMessage("This is not a valid entity!");
                return false;
            }
        }else{
            player.sendMessage("There is not entity within a reasonable distance from you!");
            return false;
        }
    }

    public static Vector calculateVelocity(Vector from, Vector to, int heightGain)
    {
        // Gravity of a potion
        double gravity = 0.15;

        // Block locations
        int endGain = to.getBlockY() - from.getBlockY();
        double horizDist = Math.sqrt(distanceSquared(from, to));

        // Height gain
        int gain = heightGain;

        double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);

        // Solve quadratic equation for velocity
        double a = -horizDist * horizDist / (4 * maxGain);
        double b = horizDist;
        double c = -endGain;

        double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);

        // Vertical velocity
        double vy = Math.sqrt(maxGain * gravity);

        // Horizontal velocity
        double vh = vy / slope;

        // Calculate horizontal direction
        int dx = to.getBlockX() - from.getBlockX();
        int dz = to.getBlockZ() - from.getBlockZ();
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = dx / mag;
        double dirz = dz / mag;

        // Horizontal velocity components
        double vx = vh * dirx;
        double vz = vh * dirz;

        return new Vector(vx, vy, vz);
    }

    private static double distanceSquared(Vector from, Vector to)
    {
        double dx = to.getBlockX() - from.getBlockX();
        double dz = to.getBlockZ() - from.getBlockZ();
        return dx * dx + dz * dz;
    }

}
