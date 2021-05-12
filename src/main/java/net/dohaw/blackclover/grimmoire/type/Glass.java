package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.glass.GlassCreation;
import net.dohaw.blackclover.grimmoire.spell.type.glass.GlassShot;
import net.dohaw.blackclover.grimmoire.spell.type.glass.Wall;

import java.util.Arrays;
import java.util.List;

public class Glass extends GrimmoireWrapper {

    public GlassCreation glassCreation;
    public GlassShot glassShot;
    public Wall wall;

    public Glass() {
        super(GrimmoireType.GLASS);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("glass");
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

        this.wall = new Wall(config);
        this.spells.put(SpellType.GLASS_WALL, wall);

        this.glassShot = new GlassShot(config);
        this.spells.put(SpellType.GLASS_SHOT, glassShot);

        this.glassCreation = new GlassCreation(config);
        this.spells.put(SpellType.GLASS_CREATION, glassCreation);

    }

}