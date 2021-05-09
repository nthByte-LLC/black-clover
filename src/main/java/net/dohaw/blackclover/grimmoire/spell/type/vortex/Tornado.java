package net.dohaw.blackclover.grimmoire.spell.type.vortex;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.VortexTornado;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;

public class Tornado extends CastSpellWrapper {

    public Tornado(GrimmoireConfig grimmoireConfig) {
        super(SpellType.TORNADO, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        Location locInFront = LocationUtil.getLocationInFront(pd.getPlayer(), 1);
        BukkitTask vt = new VortexTornado(SpellUtils.invisibleArmorStand(locInFront), new Particle.DustOptions(BukkitColor.DARK_GREY, 1), 10).runTaskTimer(Grimmoire.instance, 0L, 5L);
        return false;
    }

    @Override
    public void prepareShutdown() {

    }

}
