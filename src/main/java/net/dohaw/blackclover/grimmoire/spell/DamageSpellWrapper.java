package net.dohaw.blackclover.grimmoire.spell;

import lombok.Getter;
import net.dohaw.blackclover.config.GrimmoireConfig;

public abstract class DamageSpellWrapper extends SpellWrapper{

    @Getter
    protected double damageScale;

    public DamageSpellWrapper(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    @Override
    public void loadSettings(){
        super.loadSettings();
        this.damageScale = grimmoireConfig.getNumberSetting(KEY, "Damage Scale");
    }

}
