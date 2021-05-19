package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.PortalThresholdCrossEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.UUID;

public abstract class PortalSpell<T extends Portal> extends CastSpellWrapper implements Listener {

    protected double widthPortal, heightPortal;

    protected HashSet<UUID> hasRecentlyEnteredPortal = new HashSet<>();

    public PortalSpell(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.widthPortal = grimmoireConfig.getDoubleSetting(KEY, "Width Portal");
        this.heightPortal = grimmoireConfig.getDoubleSetting(KEY, "Height Portal");
    }

    public Location getPortalStartLocation(Player portalCreator){
        return getPortalStartLocation(portalCreator.getLocation());
    }

    public Location getPortalStartLocation(Location portalCreatorLocation){
        Location locInFront = LocationUtil.getAbsoluteLocationInFront(portalCreatorLocation.add(0, 0.1, 0), 2);
        return LocationUtil.getAbsoluteLocationToLeft(locInFront, widthPortal / 2);
    }

    protected void startPortalEnteringCooldown(Entity entity){
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            hasRecentlyEnteredPortal.remove(entity.getUniqueId());
        }, 20L * 5);
    }

    protected boolean hasEnteredPortalRecently(Entity entity){
        return hasRecentlyEnteredPortal.contains(entity.getUniqueId());
    }

    public double getWidthPortal() {
        return widthPortal;
    }

    public double getHeightPortal() {
        return heightPortal;
    }

    @Override
    public void prepareShutdown() { }

    @EventHandler
    public abstract void onEnterPortal(PortalThresholdCrossEvent<T> e);

}
