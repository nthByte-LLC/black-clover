package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.spell.type.cotton.SheepArmy;
import net.dohaw.blackclover.pathfinder.PathfinderGoalFollowEntity;
import net.dohaw.blackclover.playerdata.CottonPlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalSelector;
import org.bukkit.EntityEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Checks whether the sheep has reached it's target. If so, this cancels and the sheep explodes
 */
public class SheepArmyGoalChecker extends BukkitRunnable {

    private SheepArmy spell;
    private CottonPlayerData caster;
    private List<Sheep> army;
    private LivingEntity target;

    public SheepArmyGoalChecker(List<Sheep> army, CottonPlayerData caster, LivingEntity target, SheepArmy spell){
        this.army = army;
        this.target = target;
        this.caster = caster;
        this.spell = spell;
    }

    @Override
    public void run() {

        int sheepAlive = getNumSheepAlive();
        if(sheepAlive != 0 && target.isValid()){

            for(Sheep sheep : army){
                // repeatedly sets the target so that they don't stray off course
                sheep.setTarget(target);
                double distanceFromTarget = sheep.getLocation().distance(target.getLocation());
                if(distanceFromTarget <= spell.getExplosionDistance()){
                    Vector sheepDir = sheep.getLocation().clone().getDirection();
                    // knockback
                    target.setVelocity(sheepDir);
                    sheep.remove();
                    SpellUtils.spawnParticle(sheep, Particle.EXPLOSION_NORMAL, 20, 1, 1, 1);
                    SpellUtils.playSound(sheep, Sound.ENTITY_GENERIC_EXPLODE);
                    target.damage(spell.getDamage(), sheep);
                    target.playEffect(EntityEffect.HURT_EXPLOSION);
                }
            }

        }else{
            cancel();
        }

    }

    private int getNumSheepAlive(){
        int numSheepAlive = 0;
        for(Sheep sheep : army){
            if(sheep.isValid()){
                numSheepAlive++;
            }
        }
        return numSheepAlive;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        caster.removeArmy();
        super.cancel();
    }

}
