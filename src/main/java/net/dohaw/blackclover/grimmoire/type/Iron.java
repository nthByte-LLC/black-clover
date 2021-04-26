package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.iron.Sword;

import java.util.Arrays;
import java.util.List;

public class Iron extends GrimmoireWrapper {

    public Sword sword;

    public Iron() {
        super(GrimmoireType.IRON);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("iron");
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DEFENSE;
    }

    @Override
    public void initSpells() {

        this.sword = new Sword(config);
        this.spells.put(SpellType.SWORD, sword);

    }

}
