package net.dohaw.blackclover.playerdata;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.type.trap.Trap;
import net.dohaw.blackclover.grimmoire.spell.type.trap.TrapType;

import java.util.*;

public class TrapPlayerData extends TrapSetterPlayerData {

    private boolean reflectingSpells;

    public TrapPlayerData(UUID uuid) {
        super(uuid, Grimmoire.TRAP);
    }

    public boolean isReflectingSpells() {
        return reflectingSpells;
    }

    public void setReflectingSpells(boolean reflectingSpells) {
        this.reflectingSpells = reflectingSpells;
    }

}
