package net.dohaw.blackclover.grimmoire.spell.type.gravity;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.type.Gravity;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Gives a target the ability to float for 10 seconds. Also gives the caster slow falling.
 */
public class ZeroGravity extends CastSpellWrapper {

    public double duration;
    private int castDistance;

    public ZeroGravity(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ZERO_GRAVITY, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(player, castDistance);
        if(SpellUtils.isTargetValid(player, entityInSight)){

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, (int) (duration * 20L), 0));

            // player's can have player data and i can alter whether they're floating through this
            if(entityInSight instanceof Player){

                PlayerData targetData = Grimmoire.instance.getPlayerDataManager().getData(entityInSight.getUniqueId());
                Float floatSpell = Grimmoire.GRAVITY.floatSpell;
                floatSpell.initFloat(targetData);

                // Removes the spell after a certain period of time.
                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                    floatSpell.deactiveSpell(targetData);
                }, (long) (duration * 20L));

            }else{

                // For living entities, they float differently. They are given the levitation potion
                LivingEntity le = (LivingEntity) entityInSight;
                assert le != null;
                le.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, (int) (duration * 20L), 0));

                SpellUtils.playSound(le, Sound.BLOCK_BEACON_ACTIVATE);
                SpellUtils.spawnParticle(le, Particle.END_ROD, 30, 1, 1, 1);

            }
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
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

}
