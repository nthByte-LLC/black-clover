package net.dohaw.blackclover.grimmoire.spell.type.time;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class TimeLord extends CastSpellWrapper {

    private int radius;

    private int speedLevel, hasteLevel;
    private double speedDuration, hasteDuration;

    private double durationFrozen;

    public TimeLord(GrimmoireConfig grimmoireConfig) {
        super(SpellType.TIME_LORD, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd){

        Player player = pd.getPlayer();
        Collection<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
        for(Entity en : nearbyEntities){
            SpellUtils.freezeEntity(en, durationFrozen);
            SpellUtils.spawnParticle(en, Particle.VILLAGER_ANGRY, 30, 1, 1, 1);
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, (int) (hasteDuration * 20), hasteLevel - 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (speedDuration * 20), speedLevel - 1));
        SpellUtils.playSound(player, Sound.BLOCK_ANVIL_PLACE);

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.speedDuration = grimmoireConfig.getDoubleSetting(KEY, "Speed Duration");
        this.hasteDuration = grimmoireConfig.getDoubleSetting(KEY, "Haste Duration");
        this.speedLevel = grimmoireConfig.getIntegerSetting(KEY, "Speed Level");
        this.hasteLevel = grimmoireConfig.getIntegerSetting(KEY, "Haste Level");
        this.durationFrozen = grimmoireConfig.getDoubleSetting(KEY, "Duration Frozen");
        this.radius = grimmoireConfig.getIntegerSetting(KEY, "Radius");
    }

}
