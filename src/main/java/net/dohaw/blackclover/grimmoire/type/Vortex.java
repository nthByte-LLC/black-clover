package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.vortex.Tornado;

import java.util.Arrays;
import java.util.List;

public class Vortex extends GrimmoireWrapper {

    public Tornado tornado;

    public Vortex() {
        super(GrimmoireType.VORTEX);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("vortex");
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DAMAGE;
    }

    @Override
    public void initSpells() {
        this.tornado = new Tornado(config);
        this.spells.put(SpellType.TORNADO, tornado);
    }

}
