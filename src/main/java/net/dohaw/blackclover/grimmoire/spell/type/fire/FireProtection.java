package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.PassiveSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class FireProtection extends PassiveSpellWrapper implements Listener {

    public FireProtection(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_PROTECTION, grimmoireConfig);
    }


}
