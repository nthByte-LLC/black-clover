package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.grimmoire.Grimmoire;

import java.util.UUID;

public class GravityPlayerData extends PlayerData{

    @Getter @Setter
    private boolean isFloating;

    @Getter @Setter
    private double floatY;

    public GravityPlayerData(UUID uuid) {
        super(uuid, Grimmoire.GRAVITY);
    }

}
