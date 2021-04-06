package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.grimmoire.Grimmoire;

import java.util.UUID;

public class CottonPlayerData extends PlayerData{

    @Getter @Setter
    private boolean bedSpawned;

    public CottonPlayerData(UUID uuid) {
        super(uuid, Grimmoire.COTTON);
    }

}
