package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.spell.type.wind.Hurricane;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.UUID;

public class HurricaneRunner extends BukkitRunnable {

    private Hurricane hurricane;

    private double radius;
    private Player caster;

    public HurricaneRunner(Player caster, Hurricane hurricane){
        this.caster = caster;
        this.hurricane = hurricane;
        this.radius = hurricane.getRadius();
    }

    @Override
    public void run() {

        Collection<Entity> nearbyEntities = caster.getNearbyEntities(radius, radius, radius);
        for(Entity e : nearbyEntities){
            UUID eUUID = e.getUniqueId();

            if(!eUUID.equals(caster.getUniqueId())){

                if(e instanceof Player) {
                    Player player = (Player) e;
                    boolean isInHurricane = player.getPersistentDataContainer().has(Hurricane.NSK_MARKER, PersistentDataType.STRING);
                    if (!isInHurricane) {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.setGravity(false);
                        // Marks them in case they leave the game with setAllowFlight as true. It will be set to false upon joining...
                        player.getPersistentDataContainer().set(Hurricane.NSK_MARKER, PersistentDataType.STRING, "marker");
                        // Adds them to the list so that we can later set allowFlight to false
                        hurricane.getInHurricane().add(player.getUniqueId());
                    }
                }
                e.setVelocity(new Vector(0, 0.5, 0));

            }
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){}

}
