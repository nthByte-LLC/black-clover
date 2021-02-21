package net.dohaw.blackclover.grimmoire.spell;

import lombok.Getter;
import net.dohaw.blackclover.config.GrimmoireConfig;

public abstract class DamageSpellWrapper extends SpellWrapper{

    @Getter
    protected double damageScale;

    public DamageSpellWrapper(SpellType spellType, String spellBoundItemKey, GrimmoireConfig grimmoireConfig) {
        super(spellType, spellBoundItemKey, grimmoireConfig);
    }

}
