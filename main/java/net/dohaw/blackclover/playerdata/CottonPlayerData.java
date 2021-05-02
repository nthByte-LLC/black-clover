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
    private Sheep goldenSheep;

    @Getter @Setter
    private List<Sheep> army = new ArrayList<>();

    public CottonPlayerData(UUID uuid) {
        super(uuid, Grimmoire.COTTON);
    }

    public boolean isSheepSpawned(){
        return singleSheep != null;
    }

    public boolean isGoldenSheepSpawned(){
        return goldenSheep != null;
    }

    public boolean isArmySpawned(){
        return !army.isEmpty();
    }

    public void removeSheep(){
        if(singleSheep != null){
            singleSheep.remove();
            singleSheep = null;
        }
    }

    public void removeGoldenSheep(){
        if(goldenSheep != null){
            goldenSheep.remove();
            goldenSheep = null;
        }
    }

    public void removeArmy(){
        Iterator<Sheep> itr = army.iterator();
        while(itr.hasNext()){
            Sheep sheep = itr.next();
            sheep.remove();
            itr.remove();
        }
    }

    @Override
    public void prepareDataRemoval() {
        removeSheep();
        removeArmy();
        super.prepareDataRemoval();
    }
}
