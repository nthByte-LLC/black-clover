package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.grimmoire.Grimmoire;

import java.util.UUID;

public class AshPlayerData extends PlayerData{

    @Getter @Setter
    private boolean isInAshForm;

    public AshPlayerData(UUID uuid) {
        super(uuid, Grimmoire.ASH);
    }

}
