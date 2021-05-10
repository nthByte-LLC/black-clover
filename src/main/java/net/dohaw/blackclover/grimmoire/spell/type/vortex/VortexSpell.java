package net.dohaw.blackclover.grimmoire.spell.type.vortex;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;

public abstract class VortexSpell extends CastSpellWrapper {

    protected int tornadoMaxTravelDistance;

    public VortexSpell(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.tornadoMaxTravelDistance = grimmoireConfig.getIntegerSetting(KEY, "Tornado Max Travel Distance");
    }

    public int getTornadoMaxTravelDistance() {
        return tornadoMaxTravelDistance;
    }

}
