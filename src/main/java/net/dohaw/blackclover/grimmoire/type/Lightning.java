package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.lightning.*;

import java.util.Arrays;
import java.util.List;

public class Lightning extends GrimmoireWrapper {

    public Teleport teleport;
    public ElectricBall electricBall;
    public ElectricFire electricFire;
    public LightningBolt lightningBolt;
    public GodSpeed godSpeed;

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

        this.electricBall = new ElectricBall(config);
        this.spells.put(SpellType.ELECTRIC_BALL, electricBall);

        this.godSpeed = new GodSpeed(config);
        this.spells.put(SpellType.GOD_SPEED, godSpeed);

        this.teleport = new Teleport(config);
        this.spells.put(SpellType.TELEPORT, teleport);

    }

}
