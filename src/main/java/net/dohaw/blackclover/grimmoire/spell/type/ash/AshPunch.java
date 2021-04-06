package net.dohaw.blackclover.grimmoire.spell.type.ash;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.AshPunchRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;

public class AshPunch extends CastSpellWrapper {

    private int castDistance;
    private double damage;

    public AshPunch(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ASH_PUNCH, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(player, castDistance);
        if(entityInSight != null){
            BukkitTask task = new AshPunchRunner(pd, (LivingEntity) entityInSight, damage).runTaskTimer(Grimmoire.instance, 0L, 1L);
            pd.addSpellRunnable(KEY, task);
            return true;
        }
        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
    }
}
