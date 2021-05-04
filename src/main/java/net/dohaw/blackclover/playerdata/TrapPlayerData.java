package net.dohaw.blackclover.playerdata;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.type.trap.Trap;
import net.dohaw.blackclover.grimmoire.spell.type.trap.TrapType;

import java.util.*;

public class TrapPlayerData extends PlayerData {

    private boolean reflectingSpells;

    private Map<TrapType, List<Trap>> traps = new HashMap<>();

    public TrapPlayerData(UUID uuid) {
        super(uuid, Grimmoire.TRAP);
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

    public boolean isReflectingSpells() {
        return reflectingSpells;
    }

    public void setReflectingSpells(boolean reflectingSpells) {
        this.reflectingSpells = reflectingSpells;
    }
}
