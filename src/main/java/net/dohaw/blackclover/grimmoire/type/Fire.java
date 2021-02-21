package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.type.FireFists;

import java.util.List;

public class Fire extends GrimmoireWrapper {

    public FireFists fireFists;

    public Fire() {
        super(GrimmoireType.FIRE);
    }

    @Override
    public List<String> getAliases() {
        return null;
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
        this.fireFists = new FireFists(config);
        this.spells.put(fireFists.getKEY(), fireFists);
    }

}
