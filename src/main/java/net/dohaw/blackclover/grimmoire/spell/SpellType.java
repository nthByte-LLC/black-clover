package net.dohaw.blackclover.grimmoire.spell;

import lombok.Getter;

public enum SpellType {

    FIRE_FISTS("fire_fists", false),
    FIRE_BALL("fire_ball", false),
    FIRE_BLAST("fire_blast", false),
    FIRE_PROTECTION("fire_protection", false),
    FIRE_CONTROL("fire_control",true);

    @Getter
    private boolean isUltimate;

    @Getter
    private String configKey;

    SpellType(String configKey, boolean isUltimate){
        this.isUltimate = isUltimate;
        this.configKey = configKey;
    }

}
