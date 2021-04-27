package net.dohaw.blackclover.grimmoire.spell.type.time;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;

public class TimeBeam extends ActivatableSpellWrapper {

    public TimeBeam(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    @Override
    public void doRunnableTick(PlayerData caster) {

    }

    @Override
    public void deactiveSpell(PlayerData caster) throws UnexpectedPlayerData {

    }

    @Override
    public void prepareShutdown() {

    }
}
