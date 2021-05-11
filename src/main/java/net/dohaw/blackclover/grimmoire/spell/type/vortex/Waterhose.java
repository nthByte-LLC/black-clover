package net.dohaw.blackclover.grimmoire.spell.type.vortex;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.vortex.WaterhoseRunner;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Waterhose extends VortexSpell{

    public Waterhose(GrimmoireConfig grimmoireConfig) {
        super(SpellType.WATERHOSE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        Location locInFront = LocationUtil.getLocationInFront(player, 1);
        new WaterhoseRunner(SpellUtils.invisibleArmorStand(locInFront), this).runTaskTimer(Grimmoire.instance, 0L, 5L);
        return true;
    }

    @Override
    public void prepareShutdown() { }

}
