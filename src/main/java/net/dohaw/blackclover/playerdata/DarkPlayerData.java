package net.dohaw.blackclover.playerdata;

import net.dohaw.blackclover.grimmoire.Grimmoire;

import java.util.UUID;

public class DarkPlayerData extends PlayerData {

    private boolean isShadowBoxing;

    public DarkPlayerData(UUID uuid) {
        super(uuid, Grimmoire.DARK);
    }

    public boolean isShadowBoxing() {
        return isShadowBoxing;
    }

    public void setShadowBoxing(boolean shadowBoxing) {
        isShadowBoxing = shadowBoxing;
    }

}
