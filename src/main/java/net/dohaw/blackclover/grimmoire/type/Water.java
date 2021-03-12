package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.water.Healing;
import net.dohaw.blackclover.grimmoire.spell.type.water.WaterControl;

import java.util.Arrays;
import java.util.List;

public class Water extends GrimmoireWrapper {

    public Healing healing;
    public WaterControl waterControl;

    public Water() {
        super(GrimmoireType.WATER);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("water", "h2o");
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.HEAL_DEFENSE;
    }

    @Override
    public void initSpells() {

        this.waterControl = new WaterControl(config);
        this.spells.put(SpellType.WATER_CONTROL, waterControl);

        this.healing = new Healing(config);
        this.spells.put(SpellType.HEALING, healing);

    }

}
