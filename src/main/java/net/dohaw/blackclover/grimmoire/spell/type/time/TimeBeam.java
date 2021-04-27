package net.dohaw.blackclover.grimmoire.spell.type.time;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TimeBeam extends ActivatableSpellWrapper {

    private int slownessLevel;
    private int castDistance;

    public TimeBeam(GrimmoireConfig grimmoireConfig) {
        super(SpellType.TIME_BEAM, grimmoireConfig);
    }

    @Override
    public void doRunnableTick(PlayerData caster) {

        Player player = caster.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(null, player, castDistance);
        if(SpellUtils.isTargetValid(player, entityInSight)){
            LivingEntity target = (LivingEntity) entityInSight;
            double slownessDuration = (getRunnableInterval() * 20) + 1;
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) slownessDuration, slownessLevel - 1));
            SpellUtils.playSound(target, Sound.ITEM_ARMOR_EQUIP_CHAIN);
            SpellUtils.spawnParticle(target, Particle.VILLAGER_ANGRY, 30, 0.3f, 0.3f, 0.3f);
        }

    }

    @Override
    public void deactiveSpell(PlayerData caster){ }

    @Override
    public void prepareShutdown() { }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.slownessLevel = grimmoireConfig.getIntegerSetting(KEY, "Slowness Level");
    }
}
