package net.dohaw.blackclover.playerdata;

import net.dohaw.blackclover.grimmoire.Grimmoire;

import java.util.UUID;

public class GravityPlayerData extends PlayerData{

    private boolean isAddingWeight;
    private boolean isRemovingWeight;

    public GravityPlayerData(UUID uuid) {
        super(uuid, Grimmoire.GRAVITY);
    }

    public boolean isAddingWeight() {
        return isAddingWeight;
    }

    public void setAddingWeight(boolean addingWeight) {
        isAddingWeight = addingWeight;
    }

    public boolean isRemovingWeight() {
        return isRemovingWeight;
    }

    public void setRemovingWeight(boolean removingWeight) {
        isRemovingWeight = removingWeight;
    }

}
