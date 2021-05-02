package net.dohaw.blackclover.grimmoire.spell;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.ActiveSpellRunner;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;

/**
 * Spells that you cast once and run its course until you reach two conditiongs:
 * 1. You run out of mana
 * 2. You cancel the spell by shift left-clicking
 */
public abstract class ActivatableSpellWrapper extends CastSpellWrapper {

    public ActivatableSpellWrapper(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    protected void activateRunnable(PlayerData pd){
        BukkitTask task = new ActiveSpellRunner(pd, this).runTaskTimer(Grimmoire.instance, 0L, getRunnableInterval());
        pd.addSpellRunnables(KEY, task);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        activateRunnable(pd);
        return true;
    }

    /**
     * What you do in the runner every single second
     */
    public abstract void doRunnableTick(PlayerData caster);

    public long getRunnableInterval(){
        return 20L;
    }

    /**
     * Specific things you need to do when the spell is done running its course
     */
    public abstract void deactiveSpell(PlayerData caster) throws UnexpectedPlayerData;

}
