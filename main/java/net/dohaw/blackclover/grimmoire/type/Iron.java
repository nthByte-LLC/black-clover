package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.iron.*;
import org.bukkit.entity.ArmorStand;

import java.util.Arrays;
import java.util.List;

public class Iron extends GrimmoireWrapper {

    public Sword sword;
    public Armor armor;
    public Golem golem;
    public Extraction extraction;
    public Dome dome;

    public Iron() {
        super(GrimmoireType.IRON);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("iron");
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DEFENSE;
    }

    @Override
    public void initSpells() {

        this.sword = new Sword(config);
        this.armor = new Armor(config);
        this.golem = new Golem(config);
        this.extraction = new Extraction(config);
        this.dome = new Dome(config);
        this.spells.put(SpellType.SWORD, sword);
        this.spells.put(SpellType.ARMOR, armor);
        this.spells.put(SpellType.GOLEM, golem);
        this.spells.put(SpellType.EXTRACTION, extraction);
        this.spells.put(SpellType.DOME, dome);

    }

}
