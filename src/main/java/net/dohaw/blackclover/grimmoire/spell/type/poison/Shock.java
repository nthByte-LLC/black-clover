package net.dohaw.blackclover.grimmoire.spell.type.poison;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Shock extends CastSpellWrapper {

    private double poisonDuration;
    private int damage;
    private int poisonLevel;
    private int castDistance;

    public Shock(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SHOCK, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, entityInSight)){
            
            LivingEntity target = (LivingEntity) entityInSight;
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int) (poisonDuration * 20), poisonLevel - 1));
            SpellUtils.doSpellDamage(target, player, KEY, damage);
            SpellUtils.spawnParticle(target, Particle.CRIT_MAGIC, 30, 1, 1, 1);
            SpellUtils.playSound(target, Sound.BLOCK_DISPENSER_DISPENSE);

            Grimmoire.POISON.spawnPoisonParticles(target, poisonDuration);

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
        this.damage = grimmoireConfig.getIntegerSetting(KEY, "Damage");
        this.poisonDuration = grimmoireConfig.getDoubleSetting(KEY, "Poison Duration");
        this.poisonLevel = grimmoireConfig.getIntegerSetting(KEY, "Poison Level");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

}
