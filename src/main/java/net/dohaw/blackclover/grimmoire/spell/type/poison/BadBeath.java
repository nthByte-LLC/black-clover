package net.dohaw.blackclover.grimmoire.spell.type.poison;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class BadBeath extends CastSpellWrapper {

    private double duration;
    private int poisonLevel;
    private int castDistance;

    public BadBeath(GrimmoireConfig grimmoireConfig) {
        super(SpellType.BAD_BREATH, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        Entity targetEntity = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, targetEntity)){

            LivingEntity target = (LivingEntity) targetEntity;
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int) (duration * 20), poisonLevel - 1));

            SpellUtils.playSound(target, Sound.BLOCK_DISPENSER_DISPENSE);
            Grimmoire.POISON.spawnPoisonParticles(target, duration);

            return true;
        }
        return false;
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
        this.poisonLevel = grimmoireConfig.getIntegerSetting(KEY, "Poison Level");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

}
