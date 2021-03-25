package net.dohaw.blackclover.grimmoire.spell.type.snow;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.TimeCastable;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
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

        TornadoParticleRunner pr1 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.WHITE, 1), true, radius, false);
        pr1.setVerticalPointSpread(0.1);
        TornadoParticleRunner pr2 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.GRAY, 1), true, radius, true);
        pr2.setVerticalPointSpread(0.1);
        pd.addSpellRunnable(KEY, pr1.runTaskTimer(Grimmoire.instance, 0L, 1L), pr2.runTaskTimer(Grimmoire.instance, 0L, 1L));
        SpellUtils.playSound(player, Sound.BLOCK_BEACON_ACTIVATE);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {

            SpellUtils.playSound(player, Sound.BLOCK_ANVIL_PLACE);
            SpellUtils.spawnParticle(player, Particle.FLASH, 10, 1, 1, 1);
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
                    UUID playerAffectedUUID = playerAffected.getUniqueId();
                    // if they aren't already frozen, then add them to the list of the current spell's frozen players.
                    if(!frozenPlayers.contains(playerAffectedUUID)){
                        currentSpellFrozenPlayers.add(playerAffected.getUniqueId());
                    }
                    // same thing here
                    if(!invulnerablePlayers.contains(playerAffectedUUID)){
                        currentSpellInvulnerablePlayers.add(playerAffected.getUniqueId());
                    }
                    // i don't need to run a #contains check because hashsets don't add duplicate entries.
                    frozenPlayers.add(playerAffected.getUniqueId());
                    invulnerablePlayers.add(playerAffected.getUniqueId());

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

    // makes player invulnerable
    @EventHandler
    public void onPlayerTakeDamager(EntityDamageEvent e){
        Entity entity = e.getEntity();
        if(entity instanceof Player){
            if(invulnerablePlayers.contains(entity.getUniqueId())){
                e.setCancelled(true);
            }
        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castTime = grimmoireConfig.getDoubleSetting(KEY, "Cast Time");
        this.radius = grimmoireConfig.getDoubleSetting(KEY, "Radius");
        this.invulnerableDuration = grimmoireConfig.getDoubleSetting(KEY, "Duration Invulnerable");
        this.freezeDuration = grimmoireConfig.getDoubleSetting(KEY, "Duration Freeze");
    }

}
