package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.DamageSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.FireBlastRunner;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class FireBlast extends DamageSpellWrapper {

    private int distance;

    public FireBlast(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_BLAST, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        new FireBlastRunner(player, distance, damageScale).runTaskTimer(Grimmoire.instance, 0L, 2L);
        deductMana(pd);
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.distance = grimmoireConfig.getNumberSetting(KEY, "Distance");
    }

}
