package net.dohaw.blackclover.grimmoire.spell.type.time;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class AlterTime extends CastSpellWrapper {

    private double durationFrozen;
    private int castDistance;

    public AlterTime(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ALTER_TIME, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, entityInSight)){

            if(entityInSight instanceof Player){
                Player target = (Player) entityInSight;
                PlayerData targetPlayerData = Grimmoire.instance.getPlayerDataManager().getData(target);
                targetPlayerData.setFrozen(Grimmoire.instance, (int) durationFrozen);
            }else{
                LivingEntity target = (LivingEntity) entityInSight;
                target.setAI(false);
                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                   target.setAI(true);
                }, (long) (durationFrozen * 20));
            }

            SpellUtils.playSound(entityInSight, Sound.BLOCK_ANVIL_PLACE);
            SpellUtils.spawnParticle(entityInSight, Particle.VILLAGER_ANGRY, 30, 1, 1, 1);

            return true;

        }

        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.durationFrozen = grimmoireConfig.getDoubleSetting(KEY, "Duration Frozen");
    }

    @Override
    public void prepareShutdown() {

    }

}
