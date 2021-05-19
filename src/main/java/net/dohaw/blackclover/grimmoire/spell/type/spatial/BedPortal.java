package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BedPortal extends StandingPortal {

    private Player portalMaker;

    public BedPortal(Location bottomLeftCorner, PortalSpell portalSpell, Player portalMaker) {
        super(bottomLeftCorner, portalSpell, new Particle.DustOptions(Color.WHITE, 1));
        System.out.println("BED IS MADEEEE");
        this.portalMaker = portalMaker;
    }

    @Override
    public void teleport(Entity entity) {
        // we will check bed spawn location before-hand
        entity.teleport(portalMaker.getBedSpawnLocation());
    }

}
