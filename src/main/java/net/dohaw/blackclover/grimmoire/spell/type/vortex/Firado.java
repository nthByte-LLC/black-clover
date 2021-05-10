package net.dohaw.blackclover.grimmoire.spell.type.vortex;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.vortex.VortexTornadoSpell;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Firado extends CastSpellWrapper {

    public Firado(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRADO, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        Location locInFront = LocationUtil.getLocationInFront(player, 1);
        new VortexTornadoSpell(SpellUtils.invisibleArmorStand(locInFront), new Particle.DustOptions(Color.ORANGE, 1), SpellType., player).runTaskTimer(Grimmoire.instance, 0L, 5L);
        return true;
    }

    @Override
    public void prepareShutdown() {

    }

}
