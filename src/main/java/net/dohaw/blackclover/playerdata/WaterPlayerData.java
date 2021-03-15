package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Drowned;

import java.util.UUID;

public class WaterPlayerData extends PlayerData {

    @Getter @Setter
    private Drowned drowned;

    public WaterPlayerData(UUID uuid) {
        super(uuid);
    }

    public boolean isDrownedSummoned(){
        return drowned != null;
    }

    public void removeDrowned(){
        drowned.remove();
        drowned = null;
    }

    @Override
    public void prepareDataRemoval() {
        super.prepareDataRemoval();
        if(drowned != null){
            drowned.remove();
        }
    }

}
