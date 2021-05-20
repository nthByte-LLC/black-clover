package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.permeation.Invulnerability;
import net.dohaw.blackclover.grimmoire.spell.type.permeation.Thru;

import java.util.Arrays;
import java.util.List;

public class Permeation extends GrimmoireWrapper {

    public Invulnerability invulnerability;
    public net.dohaw.blackclover.grimmoire.spell.type.permeation.Permeation permeation;
    public Thru thru;

    public Permeation() {
        super(GrimmoireType.PERMEATION);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("permeation");
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.SPECIAL;
    }

    @Override
    public void initSpells() {

        this.thru = new Thru(config);
        this.spells.put(SpellType.THRU, thru);

        this.permeation = new net.dohaw.blackclover.grimmoire.spell.type.permeation.Permeation(config);
        this.spells.put(SpellType.PERMEATION, permeation);

        this.invulnerability = new Invulnerability(config);
        this.spells.put(SpellType.INVULNERABILITY, invulnerability);

    }

}
