package net.dohaw.blackclover.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class AttributeHelper {

    public static void alterAttribute(LivingEntity le, Attribute attribute, double additive){
        double base = le.getAttribute(attribute).getBaseValue();
        le.getAttribute(attribute).setBaseValue(base + additive);
    }

}
