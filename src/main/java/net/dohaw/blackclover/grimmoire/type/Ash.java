package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ash extends GrimmoireWrapper {

    public Ash() {
        super(GrimmoireType.ASH);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ash");
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DEFENSE;
    }

    @Override
    public void initSpells() {

    }
}
