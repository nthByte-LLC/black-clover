package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DamageableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.FireBlastRunner;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class FireBlast extends CastSpellWrapper implements DamageableSpell {

    private int distance;

    public FireBlast(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_BLAST, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        new FireBlastRunner(player, distance, damageScale).runTaskTimer(Grimmoire.instance, 0L, 2L);
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.distance = grimmoireConfig.getIntegerSetting(KEY, "Distance");
    }

    @Override
    public void prepareShutdown() {

    }

}
