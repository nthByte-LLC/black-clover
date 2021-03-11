package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.anti.DemonForm;
import net.dohaw.blackclover.grimmoire.spell.type.anti.DemonJump;
import net.dohaw.blackclover.grimmoire.spell.type.anti.DemonScratch;
import net.dohaw.blackclover.grimmoire.spell.type.anti.Disable;

import java.util.Arrays;
import java.util.List;

public class Anti extends GrimmoireWrapper {

    public DemonForm demonForm;
    public Disable disable;
    public DemonJump demonJump;
    public DemonScratch demonScratch;

    public Anti() {
        super(GrimmoireType.ANTI);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("anti");
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.SPECIAL;
    }

    @Override
    public void initSpells() {

        this.disable = new Disable(config);
        this.spells.put(SpellType.DISABLE, disable);

        this.demonJump = new DemonJump(config);
        this.spells.put(SpellType.DEMON_JUMP, demonJump);

        this.demonScratch = new DemonScratch(config);
        this.spells.put(SpellType.DEMON_SCRATCH, demonScratch);

        this.demonForm = new DemonForm(config);
        this.spells.put(SpellType.DEMON_FORM, demonForm);

    }

}
