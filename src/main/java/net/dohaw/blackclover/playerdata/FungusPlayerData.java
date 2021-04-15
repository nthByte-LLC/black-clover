package net.dohaw.blackclover.playerdata;

import net.dohaw.blackclover.grimmoire.Grimmoire;

import java.util.UUID;

public class FungusPlayerData extends PlayerData{

    private boolean isMorphed;

    public FungusPlayerData(UUID uuid) {
        super(uuid, Grimmoire.FUNGUS);
    }

    public boolean isMorphed() {
        return isMorphed;
    }

    public void setMorphed(boolean morphed) {
        isMorphed = morphed;
    }

}
