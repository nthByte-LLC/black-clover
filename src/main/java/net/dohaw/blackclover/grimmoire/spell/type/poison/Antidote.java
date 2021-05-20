package net.dohaw.blackclover.grimmoire.spell.type.poison;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

/**
 * Takes the poison off a target.
 */
public class Antidote extends CastSpellWrapper {

    private int castDistance;

    public Antidote(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ANTIDOTE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity targetEntity = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, targetEntity)){

            LivingEntity target = (LivingEntity) targetEntity;
            if(target.hasPotionEffect(PotionEffectType.POISON)){
                BukkitTask runner = new CircleParticleRunner(target, Particle.SPELL, true, 1).runTaskTimer(Grimmoire.instance, 0L, 1L);
                target.removePotionEffect(PotionEffectType.POISON);
                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, runner::cancel, 10L);
                return true;
            }else{
                player.sendMessage("This player does not have any poison!");
            }

        }

        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

}
