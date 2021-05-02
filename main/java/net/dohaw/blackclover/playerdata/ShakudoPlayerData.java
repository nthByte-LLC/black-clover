package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ShakudoPlayerData extends PlayerData {

    @Getter @Setter
    private List<Wolf> pack = new ArrayList<>();

    @Getter @Setter
    private Wolf wolf;

    @Getter @Setter
    private boolean singularWolfSpawned;

    @Getter @Setter
    private boolean packCalled;

    @Getter @Setter
    private boolean fangsEnabled;

    public ShakudoPlayerData(UUID uuid) {
        super(uuid, Grimmoire.SHAKUDO );
    }

    @Override
    public void prepareDataRemoval() {
        super.prepareDataRemoval();
        if(wolf != null){
            wolf.remove();
        }
        if(!pack.isEmpty()){
            pack.forEach(Entity::remove);
        }
    }

    public void removePack(){
        Iterator<Wolf> itr = pack.iterator();
        while(itr.hasNext()){
            Wolf wolf = itr.next();
            wolf.remove();
            itr.remove();
        }
        this.packCalled = false;
    }

}
