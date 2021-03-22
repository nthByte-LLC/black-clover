package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.plant.HealingLeaves;
import net.dohaw.blackclover.grimmoire.spell.type.plant.LeafKnives;
import net.dohaw.blackclover.grimmoire.spell.type.plant.LeafLadder;

import java.util.Arrays;
import java.util.List;

public class Plant extends GrimmoireWrapper {

    public LeafLadder leafLadder;
    public LeafKnives leafKnives;
    public HealingLeaves healingLeaves;

    public Plant() {
        super(GrimmoireType.PLANT);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("plant");
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.HEAL;
    }

    @Override
    public void initSpells() {

        this.healingLeaves = new HealingLeaves(config);
        this.spells.put(SpellType.HEALING_LEAVES, healingLeaves);

        this.leafKnives = new LeafKnives(config);
        this.spells.put(SpellType.LEAF_KNIVES, leafKnives);

        this.leafLadder = new LeafLadder(config);
        this.spells.put(SpellType.LEAF_LADDER, leafLadder);

    }

}
