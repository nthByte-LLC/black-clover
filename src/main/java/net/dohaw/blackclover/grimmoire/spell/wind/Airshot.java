package net.dohaw.blackclover.grimmoire.spell.wind;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.BeamParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

public class Airshot extends CastSpellWrapper {

    private int castDistance;
    private double forceMultiplier;
    private double damage;
    private double velocityY;

    public Airshot(GrimmoireConfig grimmoireConfig) {
        super(SpellType.AIRSHOT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player caster = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, caster, castDistance);
        if(SpellUtils.isTargetValid(caster, entityInSight)){

            LivingEntity target = (LivingEntity) entityInSight;

            SpellUtils.spawnParticle(caster, Particle.END_ROD, 30, 1 , 1, 1);
            SpellUtils.playSound(caster, Sound.ENTITY_ENDER_DRAGON_FLAP);
            SpellUtils.doSpellDamage(target, caster, KEY, damage);

            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                Vector awayVelocity = caster.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
                awayVelocity = awayVelocity.multiply(-forceMultiplier);
                awayVelocity = awayVelocity.setY(1);
                target.setVelocity(awayVelocity);
                SpellUtils.playSound(target, Sound.ENTITY_ARROW_HIT);
            }, 1);

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
        this.forceMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Force Multiplier");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.velocityY = grimmoireConfig.getDoubleSetting(KEY, "Velocity Y");
    }

}
