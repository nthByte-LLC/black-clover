package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.cotton.*;

import java.util.Arrays;
import java.util.List;

public class Cotton extends GrimmoireWrapper {

    public LifeNet lifeNet;
    public Bed bed;
    public SheepArmy sheepArmy;
    public Sheep sheep;
    public BatteringRam batteringRam;
    public Tent tent;

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

        this.sheepArmy = new SheepArmy(config);
        this.spells.put(SpellType.SHEEP_ARMY, sheepArmy);

        this.sheep = new Sheep(config);
        this.spells.put(SpellType.SHEEP, sheep);

        this.batteringRam = new BatteringRam(config);
        this.spells.put(SpellType.BATTERING_RAM, batteringRam);

        this.tent = new Tent(config);
        this.spells.put(SpellType.TENT, tent);

    }

}
