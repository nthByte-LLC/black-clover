package net.dohaw.blackclover.grimmoire.spell.type.ash;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.AshBeamRunner;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class AshBeam extends CastSpellWrapper {

    private double beamDistance;

    public AshBeam(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ASH_BEAM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        pd.addSpellRunnables(KEY, new AshBeamRunner(player, beamDistance, 2).runTaskTimer(Grimmoire.instance, 0L, 3L));
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.beamDistance = grimmoireConfig.getDoubleSetting(KEY, "Beam Distance");
    }
}
