package net.dohaw.blackclover.playerdata;

import net.dohaw.blackclover.grimmoire.Grimmoire;

import java.util.UUID;

public class SpatialPlayerData extends PlayerData{

    public SpatialPlayerData(UUID uuid) {
        super(uuid, Grimmoire.SPATIAL);
    }

}
