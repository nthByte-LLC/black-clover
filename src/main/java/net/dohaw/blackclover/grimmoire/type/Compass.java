package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.compass.*;

import java.util.Arrays;
import java.util.List;

public class Compass extends GrimmoireWrapper {

    public Pathmaker pathmaker;
    public Waypoint waypoint;
    public Cartographer cartographer;
    public Direction direction;
    public FastTravel fastTravel;

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

        this.waypoint = new Waypoint(config);
        this.spells.put(SpellType.WAYPOINT, waypoint);

        this.pathmaker = new Pathmaker(config);
        this.spells.put(SpellType.PATH_MAKER, pathmaker);

        this.fastTravel = new FastTravel(config);
        this.spells.put(SpellType.FAST_TRAVEL, fastTravel);

    }

}
