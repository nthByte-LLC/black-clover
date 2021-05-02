package net.dohaw.blackclover.pathfinder;

import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.PathfinderGoal;

public class PathfinderGoalFollowEntity extends PathfinderGoal {

    private EntityInsentient follower;
    private EntityInsentient target;
    private double speed;
    private float distanceSquared;

    public PathfinderGoalFollowEntity(EntityInsentient follower, EntityInsentient target, double speed, float distance) {
        this.follower = follower;
        this.target = target;
        this.speed = speed;
        this.distanceSquared = distance * distance;
    }

    @Override
    public boolean a() {
        return (target != null && target.isAlive() && this.follower.h(target) > (double)distanceSquared);
    }

    @Override
    public void d() {
        this.follower.getNavigation().n();
    }

    @Override
    public void e() {
        this.follower.getNavigation().a(target, this.speed);
    }

}
