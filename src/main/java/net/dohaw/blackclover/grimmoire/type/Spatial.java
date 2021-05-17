package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;

import java.util.Collections;
import java.util.List;

public class Spatial extends GrimmoireWrapper {

    public Spatial() {
        super(GrimmoireType.SPATIAL);
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("spatial");
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.SPECIAL;
    }

    @Override
    public void initSpells() {

    }

}
