package net.dohaw.blackclover.grimmoire;

import org.apache.commons.lang.StringUtils;

public enum GrimmoireType {
    FIRE,
    SAND,
    ANTI,
    WATER,
    PLANT,
    SNOW,
    ASH,
    COTTON,
    ROCK,
    GRAVITY,
    POISON,
    FUNGUS,
    COMPASS,
    WIND,
    LIGHTNING,
    IRON,
    PERMEATION,
    TRANSFORMATION,
    GLASS,
    TIME,
    SPATIAL,
    VORTEX,
    TRAP,
    DARK;

    public String toString(){
        return StringUtils.capitalize(this.name());
    }

}