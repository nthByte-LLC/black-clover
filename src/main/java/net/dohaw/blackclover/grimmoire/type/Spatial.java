package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.spatial.*;

import java.util.Collections;
import java.util.List;

public class Spatial extends GrimmoireWrapper {

    public Banish banish;
    public HomeGate homeGate;
    public Teleport teleport;
    public EndGate endGate;
    public HellGate hellGate;
    public Portals portals;

    public Spatial() {
        super(GrimmoireType.SPATIAL);
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("spatial");
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

        this.portals = new Portals(config);
        this.spells.put(SpellType.PORTALS, portals);

        this.hellGate = new HellGate(config);
        this.spells.put(SpellType.HELL_GATE, hellGate);

        this.endGate = new EndGate(config);
        this.spells.put(SpellType.END_GATE, endGate);

        this.teleport = new Teleport(config);
        this.spells.put(SpellType.TELEPORT, teleport);

        this.homeGate = new HomeGate(config);
        this.spells.put(SpellType.HOME_GATE, homeGate);

        this.banish = new Banish(config);
        this.spells.put(SpellType.BANISH, banish);

    }

}
