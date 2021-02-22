package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.DamageSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class FireBall extends DamageSpellWrapper implements Listener {

    public FireBall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_BALL, grimmoireConfig);
    }

    @Override
    public void cast(PlayerData pd) {
        Bukkit.broadcastMessage("CASTING FIRE BALL");
    }

}
