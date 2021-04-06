package net.dohaw.blackclover.util;

import net.dohaw.blackclover.pathfinder.PathfinderGoalFollowEntity;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalSelector;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class EntityUtil {

    public static void makeEntityFollow(LivingEntity entity, LivingEntity target) {
        EntityLiving entityLiving = ((CraftLivingEntity)entity).getHandle();
        EntityLiving targetLiving = ((CraftLivingEntity)target).getHandle();
        if (entityLiving instanceof EntityInsentient) {
            EntityInsentient entityInsentient = (EntityInsentient)entityLiving;
            EntityInsentient targetInsentient = (EntityInsentient)targetLiving;
            entityInsentient.goalSelector = new PathfinderGoalSelector(entityInsentient.world.getMethodProfilerSupplier());
            entityInsentient.targetSelector = new PathfinderGoalSelector(entityInsentient.world.getMethodProfilerSupplier());
            entityInsentient.goalSelector.a(0, new PathfinderGoalFloat(entityInsentient));
            entityInsentient.goalSelector.a(1, new PathfinderGoalFollowEntity(entityInsentient, targetInsentient, 1.5D, 2.0F));
        } else {
            throw new IllegalArgumentException(entityLiving.getClass().getSimpleName() + " is not an instance of an EntityInsentient.");
        }
    }

}
