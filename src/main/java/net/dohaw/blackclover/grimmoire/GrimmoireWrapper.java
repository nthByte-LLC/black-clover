package net.dohaw.blackclover.grimmoire;

import net.dohaw.blackclover.Wrapper;

import java.util.List;

public abstract class GrimmoireWrapper extends Wrapper {

    public GrimmoireWrapper(final GrimmoireType TYPE){
        super(TYPE);
    }

    public abstract List<String> getAliases();

    public abstract int getTier();

}
