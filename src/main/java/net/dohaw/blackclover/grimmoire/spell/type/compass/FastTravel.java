package net.dohaw.blackclover.grimmoire.spell.type.compass;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.DependableSpell;
import net.dohaw.blackclover.grimmoire.spell.PassiveSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Gives a speed boost whenever a player walks on a path
 */
public class FastTravel extends PassiveSpellWrapper implements DependableSpell {

    private Pathmaker pathmaker;
    private BukkitTask pathChecker;
    private int speedLevel;

    public FastTravel(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FAST_TRAVEL, grimmoireConfig);
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.speedLevel = grimmoireConfig.getIntegerSetting(KEY, "Speed Level");
    }

    @Override
    public void prepareShutdown() {
        pathChecker.cancel();
    }

    private void startPathChecker(Pathmaker pathmaker){

        this.pathChecker = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            System.out.println("PATHS: " + pathmaker.getAllPaths().keySet().toString());
            Iterator<Map.Entry<UUID, LinkedList<Location>>> itr = pathmaker.getAllPaths().entrySet().iterator();
            while(itr.hasNext()){

                Map.Entry<UUID, LinkedList<Location>> entry = itr.next();
                Player pathOwner = Bukkit.getPlayer(entry.getKey());
                if(pathOwner != null){

                    LinkedList<Location> path = entry.getValue();
                    //Do a distance check for every single element except the one most recently placed.
                    for (int i = 0; i < path.size() - 1; i++) {

                        Location loc = path.get(i);
                        double distance = pathOwner.getLocation().distance(loc);
                        // If the player is within 1.5 blocks of the path location, then give them speed
                        if(distance <= 1.5){
                            pathOwner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 21, speedLevel - 1));
                            SpellUtils.playSound(pathOwner, Sound.BLOCK_BEACON_ACTIVATE);
                            SpellUtils.spawnParticle(pathOwner, Particle.CLOUD, 30, 0.3f, 0.3f, 0.3f);
                        }
                    }

                }else{
                    // The player has left and is offline. Offload their path location data
                    itr.remove();
                }

            }

        }, 20L, 20L);

    }

    @Override
    public void initDependableData() {
        this.pathmaker = Grimmoire.COMPASS.pathmaker;
        startPathChecker(pathmaker);
    }

}
