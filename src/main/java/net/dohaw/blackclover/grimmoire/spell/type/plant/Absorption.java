package net.dohaw.blackclover.grimmoire.spell.type.plant;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.HealthStealer;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class Absorption extends CastSpellWrapper {

    private int castDistance;
    private int stealAmount;
    private double duration;
    private double stealInterval;
    
    private final int NUM_POINTS = 10;

    public Absorption(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ABSORPTION, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(player, castDistance);
        if(entityInSight != null){
            if(entityInSight instanceof LivingEntity){

                LivingEntity le = (LivingEntity) entityInSight;

                // particle line drawer
                BukkitTask particleLineDrawer = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
                    Location end = le.getLocation();
                    Location start = player.getLocation();
                    Vector dir = end.clone().subtract(start).toVector();
                    for (double i = 0; i < NUM_POINTS; i += 0.05) {
                        Vector currentDir = dir.clone().multiply(i);
                        Location particleLoc = start.clone().add(currentDir);
                        boolean isWithinABlock = particleLoc.distance(end) < 1;
                        if(!isWithinABlock){
                            SpellUtils.spawnParticle(particleLoc, Particle.REDSTONE, new Particle.DustOptions(Color.GREEN, 0.5f),30, 0, 0, 0);
                        }else{
                            break;
                        }
                    }
                },0, 1);

                // steals the health from the target and gives it to the caster.
                BukkitTask healthStealer = new HealthStealer(player, le, stealAmount, particleLineDrawer).runTaskTimer(Grimmoire.instance,0, (long) stealInterval);

                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                    healthStealer.cancel();
                    particleLineDrawer.cancel();
                }, (long) (duration * 20));

            }else{
                player.sendMessage("This is not a valid entity!");
                return false;
            }
        }else{
            player.sendMessage("There is no entity within a reasonable distance from you!");
            return false;
        }

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.stealAmount = grimmoireConfig.getIntegerSetting(KEY, "Steal Amount");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
        this.stealInterval = grimmoireConfig.getDoubleSetting(KEY, "Steal Interval");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

}
