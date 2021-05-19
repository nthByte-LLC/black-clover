package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PlayerPortal extends StandingPortal {

    private Player destinationPlayer;

    public PlayerPortal(Location bottomLeftCorner, PortalSpell portalSpell, Player destinationPlayer) {
        super(bottomLeftCorner, portalSpell, new Particle.DustOptions(Color.PURPLE, 1));
        this.destinationPlayer = destinationPlayer;
    }

    public Player getDestinationPlayer() {
        return destinationPlayer;
    }

    public void teleport(Entity entity){
        entity.teleport(destinationPlayer.getLocation());
    }

}
