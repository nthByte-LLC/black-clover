package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import org.bukkit.entity.Sheep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class CottonPlayerData extends PlayerData{

    @Getter @Setter
    private boolean bedSpawned;

    @Getter @Setter
    private Sheep singleSheep;

    @Getter @Setter
    private List<Sheep> army = new ArrayList<>();

    public CottonPlayerData(UUID uuid) {
        super(uuid, Grimmoire.COTTON);
    }

    public boolean isSheepSpawned(){
        return singleSheep != null;
    }

    public boolean isArmySpawned(){
        return !army.isEmpty();
    }

    public void removeSheep(){
        singleSheep.remove();
        singleSheep = null;
    }

    public void removeArmy(){
        Iterator<Sheep> itr = army.iterator();
        while(itr.hasNext()){
            Sheep sheep = itr.next();
            sheep.remove();
            itr.remove();
        }
    }

}
