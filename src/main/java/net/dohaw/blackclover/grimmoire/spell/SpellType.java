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
    WATER_WALL("water_wall", true),

    /*
        Plant spells
     */
    LEAF_KNIVES("leaf_knives", false),
    LEAF_WALL("leaf_wall", false),
    HEALING_LEAVES("healing_leaves", false),
    LEAF_LADDER("leaf_ladder", false),
    LEAF_ARMOR("leaf_armor", false),
    ABSORPTION("absorption", true),

    /*
        Snow spells
     */
    SNOW_BALL("snow_ball", false),
    SNOW_MAN("snow_man", false),
    ICE_SPIKE("ice_spike", false),
    FREEZE("freeze", false),
    SKATING("skating", false),
    ICE_AGE("ice_age", true),

    /*
        Ash spells
     */
    ASH_BOMB("ash_bomb", false),
    ASH_PUNCH("ash_punch", false),
    ASH_FLIGHT("ash_flight", false),
    ASH_FORM("ash_form", false),
    ASH_BEAM("ash_beam", false),
    SMOKE_BOMB("smoke_bomb", true),

    /*
        Cotton spells
     */
    BED("bed", false),
    LIFE_NET("life_net", false),
    SHEEP_ARMY("sheep_army", false),
    SHEEP("sheep", false),
    TENT("tent", false),
    BATTERING_RAM("battering_ram", true),

    /*
        Rock spells
     */
    PEBBLE("pebble", false),
    STONE("stone", false),
    ROCK("rock", false),
    WALL("wall", false),
    FORTRESS("fortress", false),
    METROID("metroid", true),

    /*
        Gravity spells
     */
    LEVITATE("levitate", false),
    REMOVE_WEIGHT("remove_weight", false),
    ADD_WEIGHT("add_weight", false),
    FLOAT("float", false),
    GRAVITY("gravity", false),
    ZERO_GRAVITY("zero_gravity", true),

    /*
        Poison spells
     */
    SHOCK("shock", false),
    BAD_BREATH("bad_breath", false),
    POISON("poison", false),
    ANTIDOTE("antidote", false),
    PLAGUE("plague", false),
    VENOM("venom", true),

    /*
        Fungus spells
     */
    SOUP("soup", false),
    MORPH("morph", false),
    GROWTH("growth", false),
    FUNGUS_FIELD("fungus_field", false),
    PHOTOSYNTHESIS("photosynthesis", false),
    FUNGUS_MEAL("fungus_meal", true);

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
