package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.type.fire.FireBall;
import net.dohaw.blackclover.grimmoire.spell.type.fire.FireBlast;
import net.dohaw.blackclover.grimmoire.spell.type.fire.FireControl;
import net.dohaw.blackclover.grimmoire.spell.type.fire.FireFists;

import java.util.Arrays;
import java.util.List;

public class Fire extends GrimmoireWrapper {

    public FireBlast fireBlast;
    public FireBall fireBall;
    public FireFists fireFists;
    public FireControl fireControl;

    public Fire() {
        super(GrimmoireType.FIRE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("fire");
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

        this.fireBall = new FireBall(config);
        this.spells.put(fireBall.getKEY(), fireBall);

        this.fireControl = new FireControl(config);
        this.spells.put(fireControl.getKEY(), fireControl);

        this.fireBlast = new FireBlast(config);
        this.spells.put(fireBlast.getKEY(), fireBlast);

    }

}
