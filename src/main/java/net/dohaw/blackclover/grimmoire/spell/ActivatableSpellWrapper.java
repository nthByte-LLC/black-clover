package net.dohaw.blackclover.grimmoire.spell;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;

public abstract class ActivatableSpellWrapper extends CastSpellWrapper {

    public ActivatableSpellWrapper(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    protected void activateRunnable(PlayerData pd){

        BukkitTask runnable = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {

            BlackCloverPlugin instance = Grimmoire.instance;
            PlayerData updatedData = instance.getPlayerDataManager().getData(pd.getUuid());

            if(updatedData.hasSufficientRegenForSpell(this)){
                deductMana(updatedData);
                instance.updateRegenBar(updatedData);
                doRunnableTick(updatedData);
            }else{
                updatedData.stopSpellRunnables(this.KEY);
            }

        }, 1, 20);

        PlayerData updatedData = Grimmoire.instance.getPlayerDataManager().getData(pd.getUuid());
        updatedData.addSpellRunnables(KEY, runnable);

    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        activateRunnable(pd);
        return true;
    }

    public abstract void doRunnableTick(PlayerData caster);

    public abstract void deactiveSpell(PlayerData caster) throws UnexpectedPlayerData;

}
