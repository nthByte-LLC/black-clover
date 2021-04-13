package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;

import java.util.Arrays;
import java.util.List;

public class Poison extends GrimmoireWrapper {

    public Poison() {
        super(GrimmoireType.POISON);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("poison");
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DAMAGE_HEAL;
    }

    @Override
    public void initSpells() {

    }

}
