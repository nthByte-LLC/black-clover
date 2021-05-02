package net.dohaw.blackclover.grimmoire.spell.type.lightning;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.util.BukkitColor;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

/**
 * Makes the player really fast.
 */
public class GodSpeed extends CastSpellWrapper {

    private int speedLevel;
    private double speedDuration;

    public GodSpeed(GrimmoireConfig grimmoireConfig) {
        super(SpellType.GOD_SPEED, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        applyGodspeed(player);
        return true;
    }

    public void applyGodspeed(Player player){

        CircleParticleRunner footParticles = new CircleParticleRunner(player, new Particle.DustOptions(BukkitColor.PALE_CYAN, 1), false, 1);
        CircleParticleRunner haloParticles = new CircleParticleRunner(player, new Particle.DustOptions(BukkitColor.PALE_CYAN, 1), false, 1);
        haloParticles.setStartYAdditive(2.3);

        BukkitTask task1 = footParticles.runTaskTimer(Grimmoire.instance, 0L, 3L);
        BukkitTask task2 = haloParticles.runTaskTimer(Grimmoire.instance, 0L, 3L);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            task1.cancel();
            task2.cancel();
        }, (long) (speedDuration * 20L));

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (speedDuration * 20), speedLevel - 1));

    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.speedDuration = grimmoireConfig.getDoubleSetting(KEY, "Speed Duration");
        this.speedLevel = grimmoireConfig.getIntegerSetting(KEY, "Speed Level");
    }

}
