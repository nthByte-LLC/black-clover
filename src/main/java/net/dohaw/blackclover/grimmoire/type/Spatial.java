package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.spatial.EndGate;
import net.dohaw.blackclover.grimmoire.spell.type.spatial.HellGate;
import net.dohaw.blackclover.grimmoire.spell.type.spatial.Portals;
import net.dohaw.blackclover.grimmoire.spell.type.spatial.Teleport;

import java.util.Collections;
import java.util.List;

public class Spatial extends GrimmoireWrapper {

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

    }

}
