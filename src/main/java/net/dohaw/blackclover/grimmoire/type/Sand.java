package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;

import java.util.Arrays;
import java.util.List;

public class Sand extends GrimmoireWrapper {

    public Sand() {
        super(GrimmoireType.SAND);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("sand");
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DEFENSE;
    }

    @Override
    public void initSpells() {

    }

}
