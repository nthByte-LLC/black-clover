package net.dohaw.blackclover.grimmoire.spell.type.vortex;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.vortex.PortnadoRunner;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Portnado extends VortexSpell{

    private int maxXAdditive;
    private int maxZAdditive;

    public Portnado(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PORTNADO, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        Location locInFront = LocationUtil.getLocationInFront(player, 1);
        new PortnadoRunner(SpellUtils.invisibleArmorStand(locInFront), this, player).runTaskTimer(Grimmoire.instance, 0L, 5L);
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.maxXAdditive = grimmoireConfig.getIntegerSetting(KEY, "Max X Additive");
        this.maxZAdditive = grimmoireConfig.getIntegerSetting(KEY, "Max Z Additive");
    }

    @Override
    public void prepareShutdown() {

    }

    public int getMaxXAdditive() {
        return maxXAdditive;
    }

    public int getMaxZAdditive() {
        return maxZAdditive;
    }

}
