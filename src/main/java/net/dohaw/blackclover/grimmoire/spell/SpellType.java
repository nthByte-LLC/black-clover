package net.dohaw.blackclover.grimmoire.spell;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

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
    EARTHQUAKE("earthquake", true),

    /*
        Anti Spells
     */
    DISABLE("disable", false),
    ANTI_SWORD("anti_sword", false),
    REFLECTION("reflection", false),
    DEMON_JUMP("demon_jump", false),
    DEMON_SCRATCH("demon_scratch", false),
    DEMON_FORM("demon_form", true),

    /*
        Shakudo Spells
     */
    WILD_CALL("wild_call", false),
    PACK("pack", false),
    FANGS("fangs", false),
    GOODIE("goodie", false),
    TAME("tame", false),
    GHOST_WOLF("ghost_wolf", true),

    /*
        Water spells
     */
    WATER_CONTROL("water_control", false),
    DROWNED("drowned", false),
    OCTOPUS("octopus", false),
    WATER_BUBBLE("water_bubble", false),
    HEALING("healing", false),
    WATER_WALL("water_wall", true);


    @Getter
    private boolean isUltimate;

    @Getter
    private String configKey;

    SpellType(String configKey, boolean isUltimate){
        this.isUltimate = isUltimate;
        this.configKey = configKey;
    }

    public String toProperName(){
        return StringUtils.capitalize(this.getConfigKey().replace("_", " "));
    }

}
