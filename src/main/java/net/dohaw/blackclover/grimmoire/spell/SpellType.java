package net.dohaw.blackclover.grimmoire.spell;

import lombok.Getter;

public enum SpellType {

    /*
        Fire Spells
     */
    FIRE_FISTS("fire_fists", false),
    FIRE_BALL("fire_ball", false),
    FIRE_BLAST("fire_blast", false),
    FIRE_PROTECTION("fire_protection", false),
    FIRE_CONTROL("fire_control",false),
    FIRE_STORM("fire_storm", true),

    /*
        Sand Spells
     */
    SAND_BLAST("sand_blast", false),
    SAND_GRAVE("sand_grave", false),
    QUICK_SAND("quick_sand", false),
    SAND_STORM("sand_storm", false),
    ANCIENT_RUIN("ancient_ruin", false),
    EARTHQUAKE("earthquake", true);

    @Getter
    private boolean isUltimate;

    @Getter
    private String configKey;

    SpellType(String configKey, boolean isUltimate){
        this.isUltimate = isUltimate;
        this.configKey = configKey;
    }

    public String toProperName(){
        return this.getConfigKey().replace("_", " ").toUpperCase();
    }

}
