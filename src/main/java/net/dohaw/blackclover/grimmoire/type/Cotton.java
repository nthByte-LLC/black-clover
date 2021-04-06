package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.cotton.Bed;
import net.dohaw.blackclover.grimmoire.spell.type.cotton.LifeNet;

import java.util.Arrays;
import java.util.List;

public class Cotton extends GrimmoireWrapper {

    public LifeNet lifeNet;
    public Bed bed;

    public Cotton() {
        super(GrimmoireType.COTTON);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("cotton");
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DEFENSE;
    }

    @Override
    public void initSpells() {

        this.bed = new Bed(config);
        this.spells.put(SpellType.BED, bed);

        this.lifeNet = new LifeNet(config);
        this.spells.put(SpellType.LIFE_NET, lifeNet);

    }

}
