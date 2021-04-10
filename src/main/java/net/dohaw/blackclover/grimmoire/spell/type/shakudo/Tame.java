package net.dohaw.blackclover.grimmoire.spell.type.shakudo;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.ResponderFactory;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;

public class Tame extends CastSpellWrapper {

    private int castDistance;

    public Tame(GrimmoireConfig grimmoireConfig) {
        super(SpellType.TAME, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(pd.getPlayer(), castDistance);

        ResponderFactory rf = new ResponderFactory(player);
        if(entityInSight != null){
            if(entityInSight instanceof Wolf){
                Wolf wolf = (Wolf) entityInSight;
                if(!wolf.isTamed()){
                    wolf.setSitting(true);
                    SpellUtils.playSound(player, Sound.ENTITY_VILLAGER_YES);
                    BukkitTask runner = new CircleParticleRunner(wolf, new Particle.DustOptions(Color.WHITE, 2), true, 2).runTaskTimer(Grimmoire.instance, 0L, 1L);
                    Bukkit.getScheduler().runTaskLater(Grimmoire.instance, runner::cancel, 40L);
                    wolf.setOwner(player);
                }else{
                    rf.sendMessage("This wolf is already tamed!");
                    return false;
                }
            }else{
                rf.sendMessage("This is not a wolf!");
                return false;
            }
        }else{
            SpellUtils.playSound(player, Sound.BLOCK_ANVIL_PLACE);
            rf.sendMessage("No wolf was found within a reasonable distance!");
            return false;
        }

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

    @Override
    public void prepareShutdown() {

    }

}
