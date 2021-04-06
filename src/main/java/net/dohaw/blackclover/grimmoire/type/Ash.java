package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.ash.*;

import java.util.Arrays;
import java.util.List;

public class Ash extends GrimmoireWrapper {

    public SmokeBomb smokeBomb;
    public AshFlight ashFlight;
    public AshBeam ashBeam;
    public AshForm ashForm;
    public AshPunch ashPunch;
    public AshBomb ashBomb;

    public Ash() {
        super(GrimmoireType.ASH);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ash");
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

        this.ashBomb = new AshBomb(config);
        this.spells.put(SpellType.ASH_BOMB, ashBomb);

        this.ashPunch = new AshPunch(config);
        this.spells.put(SpellType.ASH_PUNCH, ashPunch);

        this.ashForm = new AshForm(config);
        this.spells.put(SpellType.ASH_FORM, ashForm);

        this.ashBeam = new AshBeam(config);
        this.spells.put(SpellType.ASH_BEAM, ashBeam);

        this.ashFlight = new AshFlight(config);
        this.spells.put(SpellType.ASH_FLIGHT, ashFlight);

        this.smokeBomb = new SmokeBomb(config);
        this.spells.put(SpellType.SMOKE_BOMB, smokeBomb);

    }
}
