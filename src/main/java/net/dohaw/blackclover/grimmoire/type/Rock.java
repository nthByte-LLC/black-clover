package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.rock.Pebble;
import net.dohaw.blackclover.grimmoire.spell.type.rock.Stone;

import java.util.Arrays;
import java.util.List;

public class Rock extends GrimmoireWrapper {

    public Stone stone;
    public Pebble pebble;

    public Rock() {
        super(GrimmoireType.ROCK);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("rock");
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

        this.pebble = new Pebble(config);
        this.spells.put(SpellType.PEBBLE, pebble);

        this.stone = new Stone(config);
        this.spells.put(SpellType.STONE, stone);

    }

}
