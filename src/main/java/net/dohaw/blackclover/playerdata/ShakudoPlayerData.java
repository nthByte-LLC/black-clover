package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class ShakudoPlayerData extends PlayerData implements SpecifiableData{

    @Getter @Setter
    private boolean singularWolfSpawned;

    @Getter @Setter
    private boolean packCalled;

    public ShakudoPlayerData(UUID uuid) {
        super(uuid, Grimmoire.SHAKUDO );
    }

    @Override
    public void saveSpecifiedData(FileConfiguration config) {
        config.set("Has Singular Wolf Spawned", singularWolfSpawned);
        config.set("Has Pack Called", packCalled);
    }

    @Override
    public void loadSpecifiedData(FileConfiguration config) {
        this.singularWolfSpawned = config.getBoolean("Has Singular Wolf Spawned" ,false);
        this.packCalled = config.getBoolean("Has Pack Called", false);
    }

}
