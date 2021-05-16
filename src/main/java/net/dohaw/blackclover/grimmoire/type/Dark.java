package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.dark.ShadowBoxing;
import net.dohaw.blackclover.grimmoire.spell.type.dark.ShadowForm;
import net.dohaw.blackclover.grimmoire.spell.type.dark.ShadowTrap;

import java.util.Arrays;
import java.util.List;

public class Dark extends GrimmoireWrapper {

    public ShadowTrap shadowTrap;
    public ShadowForm shadowForm;
    public ShadowBoxing shadowBoxing;

    public Dark() {
        super(GrimmoireType.DARK);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("dark");
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DAMAGE;
    }

    @Override
    public void initSpells() {

        this.shadowBoxing = new ShadowBoxing(config);
        this.spells.put(SpellType.SHADOW_BOXING, shadowBoxing);

        this.shadowForm = new ShadowForm(config);
        this.spells.put(SpellType.SHADOW_FORM, shadowForm);

        this.shadowTrap = new ShadowTrap(config);
        this.spells.put(SpellType.SHADOW_TRAP, shadowTrap);

    }

}
