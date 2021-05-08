package net.dohaw.blackclover.grimmoire.spell.type.snow;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.StopTimedCastSpellEvent;
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
import org.bukkit.event.Listener;

import java.util.*;

public class Freeze extends CastSpellWrapper implements TimeCastable, Listener {

    private double invulnerableDuration, freezeDuration, castTime, radius;

    public Freeze(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FREEZE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();

        TornadoParticleRunner pr1 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.WHITE, 1), radius, false);
        TornadoParticleRunner pr2 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.GRAY, 1), radius, true);

        pd.addSpellRunnables(KEY, pr1.runTaskTimer(Grimmoire.instance, 0L, 1L), pr2.runTaskTimer(Grimmoire.instance, 0L, 1L));
        SpellUtils.playSound(player, Sound.BLOCK_BEACON_ACTIVATE);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {

            SpellUtils.playSound(player, Sound.BLOCK_ANVIL_PLACE);
            SpellUtils.spawnParticle(player, Particle.FLASH, 10, 1, 1, 1);
            Bukkit.getServer().getPluginManager().callEvent(new StopTimedCastSpellEvent(player, KEY, StopTimedCastSpellEvent.Cause.COMPLETE_CAST));

            Collection<Entity> entitiesInRange = player.getNearbyEntities(radius, radius, radius);
            List<UUID> frozenPlayers = new ArrayList<>();
            List<UUID> invulnerablePlayers = new ArrayList<>();
            for(Entity en : entitiesInRange){
                // we don't want the caster.
                if(en.getUniqueId().equals(player.getUniqueId())){
                    continue;
                }
                if(en instanceof Player){
                    Player playerAffected = (Player) en;
                    PlayerData playerAffectedData = Grimmoire.instance.getPlayerDataManager().getData(playerAffected.getUniqueId());
                    if(!playerAffectedData.isInVulnerable()){
                        playerAffectedData.setInVulnerable(true);
                        invulnerablePlayers.add(playerAffected.getUniqueId());
                    }
                    if(!playerAffectedData.isFrozen()){
                        playerAffectedData.setFrozen(true);
                        frozenPlayers.add(playerAffected.getUniqueId());
                    }
                }
            }

            // makes the players vulnerable again
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                for(UUID uuid : invulnerablePlayers){
                    PlayerData opData = Grimmoire.instance.getPlayerDataManager().getData(uuid);
                    opData.setInVulnerable(false);
                }
            }, (long) (invulnerableDuration * 20));

            // allows the player to move again
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                for(UUID uuid : frozenPlayers){
                    PlayerData opData = Grimmoire.instance.getPlayerDataManager().getData(uuid);
                    opData.setFrozen(false);
                }
            }, (long) (freezeDuration * 20));

        }, (long) (castTime * 20));

        return true;
    }


    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castTime = grimmoireConfig.getDoubleSetting(KEY, "Cast Time");
        this.radius = grimmoireConfig.getDoubleSetting(KEY, "Radius");
        this.invulnerableDuration = grimmoireConfig.getDoubleSetting(KEY, "Duration Invulnerable");
        this.freezeDuration = grimmoireConfig.getDoubleSetting(KEY, "Duration Freeze");
    }

    @Override
    public void prepareShutdown() {

    }

}
