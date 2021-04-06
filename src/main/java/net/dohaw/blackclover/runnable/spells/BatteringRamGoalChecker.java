package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.spell.type.cotton.BatteringRam;
import net.dohaw.blackclover.playerdata.CottonPlayerData;
import net.dohaw.blackclover.runnable.particle.EntityRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.EntityEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;

/**
 * Checks whether the golden sheep is close to it's target. If so, then it does damage
 */
public class BatteringRamGoalChecker extends EntityRunner {

    private LivingEntity target;
    private CottonPlayerData caster;
    private Sheep sheep;
    private BatteringRam spell;

    public BatteringRamGoalChecker(Sheep sheep, LivingEntity target, CottonPlayerData caster, BatteringRam spell){
        super(sheep, target);
        this.sheep = sheep;
        this.caster = caster;
        this.target = target;
        this.spell = spell;
    }

    @Override
    public void run() {

        double distance = sheep.getLocation().distance(target.getLocation());
        if (distance > 20) cancel();

        if(distance <= spell.getDamageDistance()){

            SpellUtils.playSound(sheep, Sound.ENTITY_SHEEP_SHEAR);
            SpellUtils.spawnParticle(sheep, Particle.SQUID_INK, 20, 1, 1, 1);

            target.damage(spell.getDamage(), sheep);
            target.playEffect(EntityEffect.HURT);
            caster.removeGoldenSheep();

        }

    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        caster.removeGoldenSheep();
    }
}
