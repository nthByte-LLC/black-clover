package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;

import java.util.Arrays;
import java.util.List;

public class Anti extends GrimmoireWrapper {

    public Anti() {
        super(GrimmoireType.ANTI);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("anti");
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.SPECIAL;
    }

    @Override
    public void initSpells() {

    }

}
