package net.dohaw.blackclover.grimmoire.spell.type.gravity;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AddWeight extends CastSpellWrapper {

    private int levelSlowness, levelStrength;
    private double duration;

    public AddWeight(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ADD_WEIGHT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (duration * 20), levelSlowness - 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (duration * 20), levelStrength - 1));
        SpellUtils.playSound(player, Sound.ENTITY_VILLAGER_NO);
        SpellUtils.spawnParticle(player, Particle.VILLAGER_ANGRY, 30, 1, 1, 1);
        return true;
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.levelSlowness = grimmoireConfig.getIntegerSetting(KEY, "Slowness Level");
        this.levelStrength = grimmoireConfig.getIntegerSetting(KEY, "Strength Level");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

}
