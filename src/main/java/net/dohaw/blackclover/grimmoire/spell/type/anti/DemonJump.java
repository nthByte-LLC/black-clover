package net.dohaw.blackclover.grimmoire.spell.type.anti;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DemonJump extends CastSpellWrapper {

    private int jumpPotionLevel;
    private double jumpPotionDuration, slowFallDuration;

    public DemonJump(GrimmoireConfig grimmoireConfig) {
        super(SpellType.DEMON_JUMP, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, (int) (slowFallDuration * 20), 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, (int) (jumpPotionDuration * 20),jumpPotionLevel - 1));
        CircleParticleRunner runner = new CircleParticleRunner(player, new Particle.DustOptions(Color.GREEN, 1), true, 1);
        runner.setMaxY(2);
        runner.setYIncrease(0.3);
        runner.runTaskTimer(Grimmoire.instance, 1L, 10L);
        // Lets the runner run for a certain amount of time and then cancels it.
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, runner::cancel, 50L);
        return false;
    }


    @Override
    public void loadSettings() {
        super.loadSettings();
        this.jumpPotionLevel = grimmoireConfig.getNumberSetting(KEY, "Jump Potion Level");
        this.jumpPotionDuration = grimmoireConfig.getNumberSetting(KEY, "Jump Potion Duration");
        this.slowFallDuration = grimmoireConfig.getNumberSetting(KEY, "Slow Fall Potion Duration");
    }
}
