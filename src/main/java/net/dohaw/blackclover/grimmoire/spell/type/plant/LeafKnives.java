package net.dohaw.blackclover.grimmoire.spell.type.plant;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.ResponderFactory;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class LeafKnives extends CastSpellWrapper {

    private final double NUM_POINTS = 10;

    private double knockback;
    private int damage;
    private int castDistance;

    public LeafKnives(GrimmoireConfig grimmoireConfig) {
        super(SpellType.LEAF_KNIVES, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        ResponderFactory rf = new ResponderFactory(player);
        if(entityInSight != null){
            if(entityInSight instanceof LivingEntity){

                LivingEntity le = (LivingEntity) entityInSight;

                // Gets the center of the circle
                Location loc = le.getLocation().clone();

                // values that make it positive or negative
                ThreadLocalRandom tlr = ThreadLocalRandom.current();
                double xMultiplier = tlr.nextInt(2) == 1 ? 1 : -1;
                double zMultiplier = tlr.nextInt(2) == 1 ? 1 : -1;

                double xAdditive = tlr.nextDouble(0.5, 3) * xMultiplier;
                double zAdditive = tlr.nextDouble(0.5, 3) * zMultiplier;

                Location point1 = loc.clone().add(xAdditive, 3, zAdditive);
                Location controlPoint = loc.clone().add(xAdditive, 0,0);
                Location point2 = loc.clone();

                SpellUtils.playSound(entityInSight, Sound.BLOCK_GRASS_BREAK);
                // going down
                double yAdditive = (1 / NUM_POINTS) * -1;

                Location particleLoc2 = point1.clone();
                for (double t = 0; t < 1; t+=(1 / NUM_POINTS)) {

                    double x = (Math.pow(1 - t, 2) * controlPoint.getX()) + (2 * (1 - t) * t * point1.getX()) + (Math.pow(t, 2) * point2.getX());
                    double z = (Math.pow(1 - t, 2) * controlPoint.getZ()) + (2 * (1 - t) * t * point1.getZ()) + (Math.pow(t, 2) * point2.getZ());
                    double y = particleLoc2.getY() + yAdditive;

                    Location particleLoc1 = new Location(point1.getWorld(), x, y, z);
                    particleLoc2 = new Location(point1.getWorld(), x + (xMultiplier * 0.4), y, z + (zMultiplier * 0.4));
                    Location particleLoc3 = new Location(point1.getWorld(), x + (xMultiplier * 0.8), y, z + (zMultiplier * 0.8));

                    // 3 paths of particles that are all offset evenly from each other.
                    SpellUtils.spawnParticle(particleLoc1, Particle.REDSTONE, new Particle.DustOptions(Color.GREEN, 0.5f), 10, 0, 0, 0);
                    SpellUtils.spawnParticle(particleLoc2, Particle.REDSTONE, new Particle.DustOptions(Color.GREEN, 0.5f), 10, 0, 0, 0);
                    SpellUtils.spawnParticle(particleLoc3, Particle.REDSTONE, new Particle.DustOptions(Color.GREEN, 0.5f), 10, 0, 0, 0);

                }

                double leHealth = le.getHealth();
                double leNewHealth = leHealth - damage;
                if(leNewHealth >= 0){
                    SpellDamageEvent event = new SpellDamageEvent(KEY, damage, le, pd.getPlayer());
                    if(!event.isCancelled()){
                        le.damage(damage);
                    }
                }else{
                    le.setHealth(0);
                    SpellUtils.spawnParticle(le, Particle.VILLAGER_HAPPY, 20, 1, 1, 1);
                    // If they kill someone with leaf knives then they don't use the cooldown and they don't expend mana.
                    return false;
                }

                // Knockback work.
                Vector velocity = le.getVelocity();
                velocity.setY(0.3333);

                if(xAdditive < 0){
                    velocity.setX(knockback);
                }else{
                    velocity.setX(-knockback);
                }

                if(zAdditive < 0){
                    velocity.setZ(knockback);
                }else{
                    velocity.setZ(-knockback);
                }

                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                    le.setVelocity(velocity);
                }, 2);

            }else{
                rf.sendMessage("This is not a valid entity!");
                return false;
            }
        }else{
            rf.sendMessage("There is no entity within a reasonable distance of you!");
            return false;
        }
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.damage = grimmoireConfig.getIntegerSetting(KEY, "Damage");
        this.knockback = grimmoireConfig.getDoubleSetting(KEY, "Knockback");
    }

}
