package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Wolf;

import java.util.UUID;

public class ShakudoPlayerData extends PlayerData implements SpecifiableData{

    @Getter @Setter
    private Wolf wolf;

    @Getter @Setter
    private boolean singularWolfSpawned = false;

    @Getter @Setter
    private boolean packCalled = false;

    public ShakudoPlayerData(UUID uuid) {
        super(uuid, Grimmoire.SHAKUDO );
    }

    @Override
    public void saveSpecifiedData(FileConfiguration config) {

    }

    @Override
    public void loadSpecifiedData(FileConfiguration config) {

    }

    @Override
    public void prepareDataRemoval() {
        super.prepareDataRemoval();
        if(wolf != null){
            wolf.remove();
        }
    }
}
