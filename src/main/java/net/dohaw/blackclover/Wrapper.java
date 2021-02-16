package net.dohaw.blackclover;

import lombok.Getter;

public abstract class Wrapper {

    @Getter
    protected final Enum KEY;

    public Wrapper(final Enum KEY){
        this.KEY = KEY;
    }

}
