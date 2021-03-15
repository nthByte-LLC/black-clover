package net.dohaw.blackclover.grimmoire.spell.type.water;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;

public class WaterBubble extends CastSpellWrapper {

    private int radius;

    public WaterBubble(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getIntegerSetting(KEY, "Radius");
    }
}
