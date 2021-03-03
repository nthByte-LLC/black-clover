package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.sand.*;

import java.util.Arrays;
import java.util.List;

public class Sand extends GrimmoireWrapper {

    public SandBlast sandBlast;
    public SandGrave sandGrave;
    public SandStorm sandStorm;
    public AncientRuin ancientRuin;
    public QuickSand quickSand;

    public Sand() {
        super(GrimmoireType.SAND);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("sand");
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DEFENSE;
    }

    @Override
    public void initSpells() {

        this.sandBlast = new SandBlast(config);
        this.spells.put(SpellType.SAND_BLAST, sandBlast);

        this.sandGrave = new SandGrave(config);
        this.spells.put(SpellType.SAND_GRAVE, sandGrave);

        this.sandStorm = new SandStorm(config);
        this.spells.put(SpellType.SAND_STORM, sandStorm);

        this.ancientRuin = new AncientRuin(config);
        this.spells.put(SpellType.ANCIENT_RUIN, ancientRuin);

        this.quickSand = new QuickSand(config);
        this.spells.put(SpellType.QUICK_SAND, quickSand);

    }

}
