package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;

import java.util.List;

public class Iron extends GrimmoireWrapper {

    public Iron() {
        super(GrimmoireType.IRON);
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public int getTier() {
        return 0;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return null;
    }

    @Override
    public void initSpells() {

    }

}
