package net.dohaw.blackclover.grimmoire.spell.type.plant;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.water.Healing;

/*
    Same thing as the Healing spell so I extend it.
 */
public class HealingLeaves extends Healing {

    public HealingLeaves(GrimmoireConfig grimmoireConfig) {
        super(SpellType.HEALING_LEAVES, grimmoireConfig);
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.selfHealAmount = grimmoireConfig.getIntegerSetting(KEY, "Self Heal Amount");
        this.targetHealAmount = grimmoireConfig.getIntegerSetting(KEY, "Target Heal Amount");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

    @Override
    public void prepareShutdown() {

    }

}
