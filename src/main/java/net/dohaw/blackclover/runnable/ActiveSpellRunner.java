package net.dohaw.blackclover.runnable;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ActiveSpellRunner extends BukkitRunnable {

    private PlayerData data;
    private ActivatableSpellWrapper spell;

    public ActiveSpellRunner(PlayerData data, ActivatableSpellWrapper spell){
        this.data = data;
        this.spell = spell;
    }

    @Override
    public void run() {

        Player player = data.getPlayer();
        if(player.isValid()){
            if(data.hasSufficientRegenForSpell(spell)){
                spell.deductMana(data);
                Grimmoire.instance.updateRegenBar(data);
                spell.doRunnableTick(data);
            }else{
                try {
                    spell.deactiveSpell(data);
                } catch (UnexpectedPlayerData unexpectedPlayerData) {
                    unexpectedPlayerData.printStackTrace();
                }
                data.stopSpellRunnables(spell.getKEY());
            }
        }else{
            cancel();
        }

    }

}
