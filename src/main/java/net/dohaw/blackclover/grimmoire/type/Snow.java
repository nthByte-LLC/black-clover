package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.snow.*;

import java.util.Arrays;
import java.util.List;

public class Snow extends GrimmoireWrapper {

    public IceAge iceAge;
    public Skating skating;
    public Freeze freeze;
    public IceSpike iceSpike;
    public Snowball snowball;
    public Snowman snowman;

    public Snow() {
        super(GrimmoireType.SNOW);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("snow");
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DAMAGE;
    }

    @Override
    public void initSpells() {

        this.snowball = new Snowball(config);
        this.spells.put(SpellType.SNOW_BALL, snowball);

        this.snowman = new Snowman(config);
        this.spells.put(SpellType.SNOW_MAN, snowman);

        this.iceSpike = new IceSpike(config);
        this.spells.put(SpellType.ICE_SPIKE, iceSpike);

        this.freeze = new Freeze(config);
        this.spells.put(SpellType.FREEZE, freeze);

        this.skating = new Skating(config);
        this.spells.put(SpellType.SKATING, skating);

        this.iceAge = new IceAge(config);
        this.spells.put(SpellType.ICE_AGE, iceAge);

    }

}
