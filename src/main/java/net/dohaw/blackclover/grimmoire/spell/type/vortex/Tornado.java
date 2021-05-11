package net.dohaw.blackclover.grimmoire.spell.type.vortex;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.vortex.VortexTornadoRunner;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Tornado extends VortexSpell {

    private double damage;
    private double forceMultiplier;

    public Tornado(GrimmoireConfig grimmoireConfig) {
        super(SpellType.TORNADO, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        Location locInFront = LocationUtil.getLocationInFront(player, 1);
        new VortexTornadoRunner(SpellUtils.invisibleArmorStand(locInFront), this, player).runTaskTimer(Grimmoire.instance, 0L, 5L);
        SpellUtils.playSound(player, Sound.BLOCK_CROP_BREAK);
        return true;
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.forceMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Force Multiplier");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
    }

    public double getForceMultiplier() {
        return forceMultiplier;
    }

    public double getDamage(){
        return damage;
    }

}
