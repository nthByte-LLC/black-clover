package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;

import java.util.Arrays;
import java.util.List;

public class Lightning extends GrimmoireWrapper {

    public Lightning() {
        super(GrimmoireType.LIGHTNING);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("lightning");
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DAMAGE;
    }

    @Override
    public void initSpells() {

    }

}
