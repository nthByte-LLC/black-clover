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
        pack.clear();
    }

    public void removeSingularWolf(){
        wolf.remove();
        this.wolf = null;
    }

    public boolean isSingularWolfSpawned(){
        return wolf != null && wolf.isValid();
    }

    /**
     * Whether there is at least 1 wolf in the pack alive
     */
    public boolean isPackStanding(){

        if(pack.isEmpty()) return false;

        int numWolvesAlive = 0;
        for (Wolf wolf1 : pack) {
            if(wolf1.isValid()){
                numWolvesAlive++;
            }
        }

        return numWolvesAlive != 0;

    }

}
