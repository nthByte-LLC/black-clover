package net.dohaw.blackclover.grimmoire.spell.type.ash;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitTask;

public class AshFlight extends CastSpellWrapper implements Listener {

    private int maxFlightDistance;
    private int heightGain;

    public AshFlight(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ASH_FLIGHT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Block targetBlock = player.getTargetBlockExact(maxFlightDistance);
        if(targetBlock != null){

            SpellUtils.playSound(player, Sound.ENTITY_ENDER_DRAGON_FLAP);
            SpellUtils.spawnParticle(player, Particle.SQUID_INK, 20, 1, 1, 1);

            ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(targetBlock.getLocation().add(0, 1, 0), EntityType.ARMOR_STAND);
            stand.setVisible(false);
            startCloudParticles(stand);

            pd.setWillTakeFallDamage(false);
            // Makes them take fall damage after 10 seconds
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
               pd.setWillTakeFallDamage(true);
            }, 20L * 10L);

            player.setVelocity(SpellUtils.calculateVelocity(player.getLocation().toVector(), targetBlock.getLocation().toVector(), heightGain));

            return true;
        }else{
            player.sendMessage("This block is out of your reach!");
        }

        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.maxFlightDistance = grimmoireConfig.getIntegerSetting(KEY, "Max Flight Distance");
        this.heightGain = grimmoireConfig.getIntegerSetting(KEY, "Height Gain");
    }

    @Override
    public void prepareShutdown() { }

    private void startCloudParticles(ArmorStand stand){

        // Creates the cloud under the location that the player is going to land on.
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLACK, 3);
        BukkitTask particleTask = new CircleParticleRunner(stand, new Particle.DustOptions(Color.BLACK, 3), false, 2).runTaskTimer(Grimmoire.instance, 0L, 3L);
        BukkitTask moreParticlesTask = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            SpellUtils.spawnParticle(stand.getLocation(), Particle.REDSTONE, dustOptions, 30, 1, 1, 1);
        }, 0L, 10L);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            stand.remove();
            particleTask.cancel();
            moreParticlesTask.cancel();
        }, 20L * 5L);

    }

    @EventHandler
    public void onTakeFallDamage(EntityDamageEvent e){
        Entity entity = e.getEntity();
        if(entity instanceof Player){
            Player player = (Player) entity;
            if(e.getCause() == EntityDamageEvent.DamageCause.FALL){
                PlayerData data = Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
                if(data != null){
                    if(!data.isWillTakeFallDamage() && data.getGrimmoireWrapper().getKEY() == GrimmoireType.ASH){
                        data.setWillTakeFallDamage(true);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}
