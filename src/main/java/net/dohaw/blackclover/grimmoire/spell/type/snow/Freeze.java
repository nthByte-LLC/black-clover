package net.dohaw.blackclover.grimmoire.spell.type.snow;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.TimeCastable;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class Freeze extends CastSpellWrapper implements TimeCastable, Listener {

    private HashSet<UUID> frozenPlayers = new HashSet<>();
    private HashSet<UUID> invulnerablePlayers = new HashSet<>();

    private double invulnerableDuration, freezeDuration, castTime, radius;

    public Freeze(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FREEZE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();

        TornadoParticleRunner pr1 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.WHITE, 0.5f), true, 1, false);
        TornadoParticleRunner pr2 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.GRAY, 0.5f), true, 1, true);
        pd.addSpellRunnable(KEY, pr1.runTaskTimer(Grimmoire.instance, 0L, 2L), pr2.runTaskTimer(Grimmoire.instance, 0L, 2L));

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {

            pd.stopTimedCast();
            Collection<Entity> entitiesInRange = player.getNearbyEntities(radius, radius, radius);
            List<UUID> currentSpellFrozenPlayers = new ArrayList<>();
            List<UUID> currentSpellInvulnerablePlayers = new ArrayList<>();

            for(Entity en : entitiesInRange){
                // we don't want the caster.
                if(en.getUniqueId().equals(player.getUniqueId())){
                    continue;
                }
                if(en instanceof Player){
                    Player playerAffected = (Player) en;
                    frozenPlayers.add(playerAffected.getUniqueId());
                    invulnerablePlayers.add(playerAffected.getUniqueId());
                    currentSpellInvulnerablePlayers.add(playerAffected.getUniqueId());
                    currentSpellFrozenPlayers.add(playerAffected.getUniqueId());
                }
            }

            // makes the players vulnerable again
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                currentSpellInvulnerablePlayers.forEach(uuid -> invulnerablePlayers.remove(uuid));
            }, (long) (invulnerableDuration * 20));

            // allows the player to move again
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                currentSpellFrozenPlayers.forEach(uuid -> frozenPlayers.remove(uuid));
            }, (long) (freezeDuration * 20));

        }, (long) (castTime * 20));

        return true;
    }

    // Freezes players
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){

        Location from = e.getFrom();
        Location to = e.getTo();
        if(frozenPlayers.contains(e.getPlayer().getUniqueId())){
            if(to != null){
                if(from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()){
                    e.setCancelled(true);
                }
            }
        }

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castTime = grimmoireConfig.getDoubleSetting(KEY, "Cast Time");
        this.radius = grimmoireConfig.getDoubleSetting(KEY, "Radius");
        this.invulnerableDuration = grimmoireConfig.getDoubleSetting(KEY, "Invulnerable Duration");
        this.freezeDuration = grimmoireConfig.getDoubleSetting(KEY, "Freeze Duration");
    }

}
