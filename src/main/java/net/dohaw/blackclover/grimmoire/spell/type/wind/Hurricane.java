package net.dohaw.blackclover.grimmoire.spell.type.wind;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.runnable.spells.HurricaneRunner;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//TODO: Make hurricane particles rise up
public class Hurricane extends CastSpellWrapper implements Listener {

    public static final NamespacedKey NSK_MARKER = NamespacedKey.minecraft("in_hurricane_marker");

    private List<UUID> inHurricane = new ArrayList<>();

    private double duration;
    private double radius;

    public Hurricane(GrimmoireConfig grimmoireConfig) {
        super(SpellType.HURRICANE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        HurricaneRunner runner = new HurricaneRunner(player, this);
        pd.addSpellRunnables(KEY, runner.runTaskTimer(Grimmoire.instance, 0L, 20L));

        for (int i = 0; i < radius; i++) {
            CircleParticleRunner cpr = new CircleParticleRunner(player, new Particle.DustOptions(Color.WHITE, 1.5f), true, i);
            pd.addSpellRunnables(KEY, cpr.runTaskTimer(Grimmoire.instance, 0L, 5L));
        }

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            pd.stopSpellRunnables(KEY);
            for(UUID uuid : inHurricane){
                Player playerInHurricane = Bukkit.getPlayer(uuid);
                if(playerInHurricane != null){
                    playerInHurricane.setFlying(false);
                    playerInHurricane.setAllowFlight(false);
                    playerInHurricane.setGravity(true);
                    playerInHurricane.getPersistentDataContainer().remove(NSK_MARKER);
                }
            }
            inHurricane.clear();
        }, (long) (duration * 20L));

        return false;
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getDoubleSetting(KEY, "Radius");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

    public List<UUID> getInHurricane() {
        return inHurricane;
    }

    public double getRadius() {
        return radius;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){

        Location to = e.getTo();
        Location from = e.getFrom();
        Player player = e.getPlayer();
        // Lets players in the hurricane move vertically, but not laterally.
        if(inHurricane.contains(player.getUniqueId()) && to != null){
            if(to.getX() != from.getX() || to.getZ() != from.getZ()){
                e.setCancelled(true);
            }
        }

    }

}
