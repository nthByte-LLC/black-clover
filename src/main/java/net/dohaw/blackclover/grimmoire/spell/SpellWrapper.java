package net.dohaw.blackclover.grimmoire.spell;

import lombok.Getter;
import net.dohaw.blackclover.Wrapper;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;

public abstract class SpellWrapper extends Wrapper {

    @Getter
    private GrimmoireWrapper grimmoire;

    @Getter
    private String customItemBoundTo;

    public SpellWrapper(SpellType spellType, String customItemBoundTo, GrimmoireWrapper grimmoire) {
        super(spellType);
        this.customItemBoundTo = customItemBoundTo;
        this.grimmoire = grimmoire;
        loadSettings();
    }

    public abstract void cast(PlayerData pd);

    public abstract void loadSettings();

}
