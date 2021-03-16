package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import org.bukkit.entity.Drowned;

import java.util.UUID;

public class WaterPlayerData extends PlayerData {

    @Getter @Setter
    private boolean usingOctopus;

    @Getter @Setter
    private Drowned drowned;

    public WaterPlayerData(UUID uuid) {
        super(uuid, Grimmoire.WATER);
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
