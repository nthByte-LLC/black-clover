package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.fungus.FungusField;
import net.dohaw.blackclover.grimmoire.spell.type.fungus.FungusMeal;
import net.dohaw.blackclover.grimmoire.spell.type.fungus.Morph;
import net.dohaw.blackclover.grimmoire.spell.type.fungus.Soup;

import java.util.Arrays;
import java.util.List;

public class Fungus extends GrimmoireWrapper {

    public FungusField fungusField;
    public Morph morph;
    public Soup soup;
    public FungusMeal fungusMeal;

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

        this.morph = new Morph(config);
        this.spells.put(SpellType.MORPH, morph);

        this.fungusField = new FungusField(config);
        this.spells.put(SpellType.FUNGUS_FIELD, fungusField);

        this.fungusMeal = new FungusMeal(config);
        this.spells.put(SpellType.FUNGUS_MEAL, fungusMeal);

    }

}
