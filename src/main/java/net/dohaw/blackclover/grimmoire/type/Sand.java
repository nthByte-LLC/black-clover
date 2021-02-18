package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;

import java.util.ArrayList;
import java.util.List;

public class Sand extends GrimmoireWrapper {

    public Sand() {
        super(GrimmoireType.SAND);
    }

    @Override
    public List<String> getAliases() {
        return null;
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
    public List<SpellType> getSpells() {
        return new ArrayList<SpellType>(){{
            add(SpellType.FIRE_BALL);
            add(SpellType.FIRE_CONTROL);
        }};
    }

}
