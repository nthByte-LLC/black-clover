package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.shakudo.*;

import java.util.Arrays;
import java.util.List;

public class Shakudo extends GrimmoireWrapper {

    public GhostWolf ghostWolf;
    public Tame tame;
    public Goodie goodie;
    public Fangs fangs;
    public WildCall wildCall;
    public Pack pack;

    public Shakudo() {
        super(GrimmoireType.SHAKUDO);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("shakudo", "shak", "animalperson");
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.SPECIAL;
    }

    @Override
    public void initSpells() {

        this.wildCall = new WildCall(config);
        this.spells.put(SpellType.WILD_CALL, wildCall);

        this.pack = new Pack(config);
        this.spells.put(SpellType.PACK, pack);

        this.fangs = new Fangs(config);
        this.spells.put(SpellType.FANGS, fangs);

        this.goodie = new Goodie(config);
        this.spells.put(SpellType.GOODIE, goodie);

        this.tame = new Tame(config);
        this.spells.put(SpellType.TAME, tame);

        this.ghostWolf = new GhostWolf(config);
        this.spells.put(SpellType.GHOST_WOLF, ghostWolf);

    }

}
