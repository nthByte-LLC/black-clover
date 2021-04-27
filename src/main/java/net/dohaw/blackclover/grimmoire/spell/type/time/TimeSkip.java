package net.dohaw.blackclover.grimmoire.spell.type.time;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Allows to you teleport behind a target and do damage to them
 */
public class TimeSkip extends CastSpellWrapper {

    private double damage;
    private int castDistance;

    public TimeSkip(GrimmoireConfig grimmoireConfig) {
        super(SpellType.TIME_SKIP, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, entityInSight)){

            LivingEntity target = (LivingEntity) entityInSight;
            Grimmoire.LIGHTNING.teleport.teleportPlayer(player, target);

            SpellUtils.doSpellDamage(target, player, KEY, damage);

            SpellUtils.spawnParticle(target, Particle.HEART, 10, 0.5f, 0.5f, 0.5f);
            SpellUtils.playSound(player, Sound.BLOCK_GLASS_BREAK);

            return true;
        }

        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

    @Override
    public void prepareShutdown() {

    }

}
