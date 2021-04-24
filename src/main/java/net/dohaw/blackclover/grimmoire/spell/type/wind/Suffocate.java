package net.dohaw.blackclover.grimmoire.spell.type.wind;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.SuffocateRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Sort of does a star wars force choke. Hoists the target in the air and starts damaging/suffocating them
 */
public class Suffocate extends CastSpellWrapper {

    private double damage;
    private int castDistance;

    public Suffocate(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SUFFOCATE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, entityInSight)){

            if(entityInSight instanceof Player){

                Player target = (Player) entityInSight;
                PlayerData targetData = Grimmoire.instance.getPlayerDataManager().getData(target.getUniqueId());
                target.setGravity(false);
                targetData.setFrozen(true);
                targetData.setCanCast(false);

                new SuffocateRunner(player, target, damage).runTaskTimer(Grimmoire.instance, 0L, 20L);
                return true;
            }else{
                player.sendMessage("This is not a valid player!");
            }

        }

        return false;
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
    }
}
