package net.dohaw.blackclover;

import lombok.Getter;

public abstract class Wrapper<K extends Enum<K>> {

    @Getter
    protected final K KEY;

    public Wrapper(final K KEY){
        this.KEY = KEY;
    }

}
