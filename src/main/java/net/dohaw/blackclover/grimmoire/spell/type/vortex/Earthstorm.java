package net.dohaw.blackclover.grimmoire.spell.type.vortex;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.vortex.EarthstormRunner;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Earthstorm extends VortexSpell{

    private double slownessDuration;
    private int slownessLevel;

    public Earthstorm(GrimmoireConfig grimmoireConfig) {
        super(SpellType.EARTHSTORM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        Location locInFront = LocationUtil.getLocationInFront(player, 1);
        new EarthstormRunner(SpellUtils.invisibleArmorStand(locInFront), this, player).runTaskTimer(Grimmoire.instance, 0L, 5L);
        SpellUtils.playSound(player, Sound.BLOCK_GRASS_PLACE);
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.slownessDuration = grimmoireConfig.getDoubleSetting(KEY, "Slowness Duration");
        this.slownessLevel = grimmoireConfig.getIntegerSetting(KEY, "Slowness Level");
    }

    public double getSlownessDuration() {
        return slownessDuration;
    }

    public int getSlownessLevel() {
        return slownessLevel;
    }

}
