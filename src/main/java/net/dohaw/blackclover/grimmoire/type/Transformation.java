package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.transformation.MorphMenuType;
import net.dohaw.blackclover.grimmoire.spell.type.transformation.MorphSpell;
import net.dohaw.blackclover.menu.transformation.HostileMobsMenu;
import net.dohaw.blackclover.menu.transformation.PassiveMobsMenu;
import net.dohaw.blackclover.menu.transformation.TransformationPlayerMenu;

import java.util.*;

public class Transformation extends GrimmoireWrapper {

    public MorphSpell<TransformationPlayerMenu> mime;
    public MorphSpell<TransformationPlayerMenu> changeling;
    public MorphSpell<PassiveMobsMenu> animorpher;
    public MorphSpell<HostileMobsMenu> monstopher;

    public Transformation() {
        super(GrimmoireType.TRANSFORMATION);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("transformation");
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.SPECIAL;
    }

    @Override
    public void initSpells() {

        this.animorpher = new MorphSpell<>(SpellType.ANIMORPHER, config, MorphMenuType.PASSIVE_MOB);
        this.spells.put(SpellType.ANIMORPHER, animorpher);

        this.monstopher = new MorphSpell<>(SpellType.MONSTOPHER, config, MorphMenuType.HOSTILE_MOB);
        this.spells.put(SpellType.MONSTOPHER, monstopher);

        this.mime = new MorphSpell<>(SpellType.MIME, config, MorphMenuType.SELF_TO_PLAYER);
        this.spells.put(SpellType.MIME, mime);

        this.changeling = new MorphSpell<>(SpellType.CHANGELING, config, MorphMenuType.PLAYER_TO_PLAYER);
        this.spells.put(SpellType.CHANGELING, changeling);

    }

}
