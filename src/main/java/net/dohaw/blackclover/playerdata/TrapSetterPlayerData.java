package net.dohaw.blackclover.playerdata;

import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.type.trap.Trap;
import net.dohaw.blackclover.grimmoire.spell.type.trap.TrapType;

import java.util.*;

public abstract class TrapSetterPlayerData extends PlayerData{

    private Map<TrapType, List<Trap>> traps = new HashMap<>();

    public TrapSetterPlayerData(UUID uuid, GrimmoireWrapper grimmoire) {
        super(uuid, grimmoire);
    }

    public void addTrap(Trap trap){

        List<Trap> currentTraps = new ArrayList<>();
        TrapType trapType = trap.getSpell().getTrapType();
        if(traps.containsKey(trapType)){
            currentTraps = traps.get(trapType);
        }

        currentTraps.add(trap);
        traps.put(trapType, currentTraps);

    }

    public void removeTrap(Trap trap){
        TrapType trapType = trap.getSpell().getTrapType();
        List<Trap> currentTraps = traps.get(trapType);
        currentTraps.remove(trap);
        trap.removeTrap();
    }

    public Map<TrapType, List<Trap>> getTraps() {
        return traps;
    }

    public int getNumTraps(TrapType type){
        List<Trap> currentTraps = traps.get(type);
        if(currentTraps != null){
            return traps.get(type).size();
        }
        return 0;
    }

    @Override
    public void prepareDataRemoval() {
        super.prepareDataRemoval();
        for(List<Trap> currentTraps : traps.values()){
            currentTraps.forEach(Trap::removeTrap);
        }
    }

}
