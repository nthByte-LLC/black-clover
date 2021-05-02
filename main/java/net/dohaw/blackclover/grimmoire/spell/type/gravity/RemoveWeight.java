package net.dohaw.blackclover.grimmoire.spell.type.gravity;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RemoveWeight extends CastSpellWrapper {

    private int levelSpeed, levelWeakness;
    private double duration;

    public RemoveWeight(GrimmoireConfig grimmoireConfig) {
        super(SpellType.REMOVE_WEIGHT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (duration * 20), levelSpeed));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (int) (duration * 20), levelWeakness));
        SpellUtils.spawnParticle(player.getLocation(), Particle.REDSTONE, new Particle.DustOptions(Color.PURPLE, 1), 30, 1, 1, 1);
        SpellUtils.playSound(player, Sound.BLOCK_PUMPKIN_CARVE);
        return true;
    }

    @Override
    public void prepareShutdown() { }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
        this.levelWeakness = grimmoireConfig.getIntegerSetting(KEY, "Weakness Level") - 1;
        this.levelSpeed = grimmoireConfig.getIntegerSetting(KEY, "Speed Level") - 1;
    }

}
