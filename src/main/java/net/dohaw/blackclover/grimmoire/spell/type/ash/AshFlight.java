package net.dohaw.blackclover.grimmoire.spell.type.ash;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;

public class AshFlight extends CastSpellWrapper {

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
            SpellUtils.spawnParticle(player, Particle.ASH, 30, 1, 1, 1);

            ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(targetBlock.getLocation().add(0, 1, 0), EntityType.ARMOR_STAND);
            stand.setVisible(false);

            BukkitTask particleTask = new CircleParticleRunner(stand, new Particle.DustOptions(Color.BLACK, 3), false, 1).runTaskTimer(Grimmoire.instance, 0L, 3L);
            Bukkit.getScheduler().runTask(Grimmoire.instance, () -> {
               stand.remove();
               particleTask.cancel();
            });

            player.setVelocity(SpellUtils.calculateVelocity(player.getLocation().toVector(), targetBlock.getLocation().toVector(), heightGain));

            return true;
        }

        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.maxFlightDistance = grimmoireConfig.getIntegerSetting(KEY, "Max Flight Distance");
        this.heightGain = grimmoireConfig.getIntegerSetting(KEY, "Height Gain");
    }
}
