package net.dohaw.blackclover.grimmoire.spell.type.vortex;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.vortex.VortexTornadoSpell;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Tornado extends CastSpellWrapper {

    private double damage;
    private double forceMultiplier;
    private int maxTravelDistance;

    public Tornado(GrimmoireConfig grimmoireConfig) {
        super(SpellType.TORNADO, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        Location locInFront = LocationUtil.getLocationInFront(player, 1);
        new VortexTornadoSpell(SpellUtils.invisibleArmorStand(locInFront), new Particle.DustOptions(BukkitColor.DARK_GREY, 1), this, player).runTaskTimer(Grimmoire.instance, 0L, 5L);
        return true;
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.maxTravelDistance = grimmoireConfig.getIntegerSetting(KEY, "Maximum Travel Distance");
        this.forceMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Force Multiplier");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
    }

    public int getMaxTravelDistance() {
        return maxTravelDistance;
    }

    public double getForceMultiplier() {
        return forceMultiplier;
    }

    public double getDamage(){
        return damage;
    }

}
