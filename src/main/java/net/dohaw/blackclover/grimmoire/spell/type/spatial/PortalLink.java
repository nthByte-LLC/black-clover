package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import org.bukkit.Location;

public class PortalLink {

    private Location firstPortal, secondPortal;

    public PortalLink(Location firstPortal, Location secondPortal){
        this.firstPortal = firstPortal;
        this.secondPortal = secondPortal;
    }

    public Location getFirstPortal() {
        return firstPortal;
    }

    public Location getSecondPortal() {
        return secondPortal;
    }

}
