package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.lightning.ElectricFire;
import net.dohaw.blackclover.grimmoire.spell.type.lightning.LightningBolt;

import java.util.Arrays;
import java.util.List;

public class Lightning extends GrimmoireWrapper {

    public ElectricFire electricFire;
    public LightningBolt lightningBolt;

    public Lightning() {
        super(GrimmoireType.LIGHTNING);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("lightning");
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DAMAGE;
    }

    @Override
    public void initSpells() {

        this.lightningBolt = new LightningBolt(config);
        this.spells.put(SpellType.LIGHTNING_BOLT, lightningBolt);

        this.electricFire = new ElectricFire(config);
        this.spells.put(SpellType.ELECTRIC_FIRE, electricFire);

    }

}
