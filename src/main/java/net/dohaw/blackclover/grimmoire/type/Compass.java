package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.compass.Cartographer;
import net.dohaw.blackclover.grimmoire.spell.type.compass.Direction;

import java.util.Arrays;
import java.util.List;

public class Compass extends GrimmoireWrapper {

    public Cartographer cartographer;
    public Direction direction;

    public Compass() {
        super(GrimmoireType.COMPASS);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("compass");
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.SPECIAL;
    }

    @Override
    public void initSpells() {

        this.direction = new Direction(config);
        this.spells.put(SpellType.DIRECTION, direction);

        this.cartographer = new Cartographer(config);
        this.spells.put(SpellType.CARTOGRAPHER, cartographer);

    }

}
