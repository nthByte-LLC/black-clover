package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.fungus.Soup;

import java.util.Arrays;
import java.util.List;

public class Fungus extends GrimmoireWrapper {

    public Soup soup;

    public Fungus() {
        super(GrimmoireType.FUNGUS);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("fungus");
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.HEAL;
    }

    @Override
    public void initSpells() {

        this.soup = new Soup(config);
        this.spells.put(SpellType.SOUP, soup);

    }

}
