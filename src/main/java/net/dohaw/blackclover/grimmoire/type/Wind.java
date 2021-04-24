package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.wind.Airshot;
import net.dohaw.blackclover.grimmoire.spell.wind.Pull;
import net.dohaw.blackclover.grimmoire.spell.wind.Push;
import net.dohaw.blackclover.grimmoire.spell.wind.Suffocate;

import java.util.Arrays;
import java.util.List;

public class Wind extends GrimmoireWrapper {

    public Suffocate suffocate;
    public Airshot airshot;
    public Pull pull;
    public Push push;

    public Wind() {
        super(GrimmoireType.WIND);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("wind");
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

        this.push = new Push(config);
        this.spells.put(SpellType.PUSH, push);

        this.pull = new Pull(config);
        this.spells.put(SpellType.PULL, pull);

        this.airshot = new Airshot(config);
        this.spells.put(SpellType.AIRSHOT, airshot);

        this.suffocate = new Suffocate(config);
        this.spells.put(SpellType.SUFFOCATE, suffocate);

    }

}
