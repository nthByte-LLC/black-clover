package net.dohaw.blackclover.grimmoire.spell;

import lombok.Getter;

public enum SpellType {

    FIRE_FIRSTS(false),
    FIRE_BALL(false),
    FIRE_BLAST(false),
    FIRE_PROTECTION(false),
    FIRE_CONTROL(true);

    @Getter
    private boolean isUltimate;
    SpellType(boolean isUltimate){
        this.isUltimate = isUltimate;
    }

}
