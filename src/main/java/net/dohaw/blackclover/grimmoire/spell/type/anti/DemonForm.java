package net.dohaw.blackclover.grimmoire.spell.type.anti;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class DemonForm extends CastSpellWrapper {

    private int duration;
    private int strengthLevel, jumpLevel, speedLevel, strengthDuration, jumpDuration, speedDuration;

    public DemonForm(GrimmoireConfig grimmoireConfig) {
        super(SpellType.DEMON_FORM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        player.setGlowing(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, jumpDuration * 20, jumpLevel - 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedDuration * 20, speedLevel - 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, strengthDuration * 20, strengthLevel - 1));

        BukkitTask tornadoParticleRunner = new TornadoParticleRunner(player, new Particle.DustOptions(BukkitColor.DARK_GREY, 1), 1, false).runTaskTimer(Grimmoire.instance, 0L, 1L);
        BukkitTask tornadoParticleRunner2 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.RED, 1), 1, true).runTaskTimer(Grimmoire.instance, 0L, 1L);

        CircleParticleRunner circleParticleRunner = new CircleParticleRunner(player, new Particle.DustOptions(BukkitColor.DARK_GREY, 1), true, 1);
        circleParticleRunner.setMaxYAdditive(1);

        CircleParticleRunner circleParticleRunner2 = new CircleParticleRunner(player, new Particle.DustOptions(Color.RED, 1), true, 1.5);
        circleParticleRunner2.setMaxYAdditive(0.5);

        SpellUtils.playSound(player, Sound.BLOCK_PUMPKIN_CARVE);
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            tornadoParticleRunner.cancel();
            tornadoParticleRunner2.cancel();
            circleParticleRunner2.runTaskTimer(Grimmoire.instance, 0L, 3L);
            circleParticleRunner.runTaskTimer(Grimmoire.instance, 0L, 3L);
            SpellUtils.spawnParticle(player, Particle.FLASH, 20, 1, 1, 1);
            SpellUtils.playSound(player, Sound.BLOCK_BEACON_ACTIVATE);
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                player.removePotionEffect(PotionEffectType.JUMP);
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.removePotionEffect(PotionEffectType.SPEED);
                player.setGlowing(false);
                circleParticleRunner.cancel();
                circleParticleRunner2.cancel();
            }, duration * 20);
        }, 20L);

        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.strengthLevel = grimmoireConfig.getIntegerSetting(KEY, "Strength Potion Level");
        this.speedLevel = grimmoireConfig.getIntegerSetting(KEY, "Speed Potion Level");
        this.jumpLevel = grimmoireConfig.getIntegerSetting(KEY, "Jump Potion Level");
        this.strengthDuration = grimmoireConfig.getIntegerSetting(KEY, "Strength Potion Duration");
        this.jumpDuration = grimmoireConfig.getIntegerSetting(KEY, "Jump Potion Duration");
        this.speedDuration = grimmoireConfig.getIntegerSetting(KEY, "Speed Potion Duration");
        this.duration = grimmoireConfig.getIntegerSetting(KEY, "Duration");
    }

}
