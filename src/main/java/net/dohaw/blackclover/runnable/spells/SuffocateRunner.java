package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.EntityRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class SuffocateRunner extends EntityRunner {

    private Player caster, target;
    // How many times the player will be teleported upwards and damaged
    private final int NUM_RUNS = 3;
    private int count = 0;

    private double damage;

    public SuffocateRunner(Player caster, Player target, double damage){
        super(caster, target);
        this.damage = damage;
        this.caster = caster;
        this.target = target;
    }

    @Override
    public void run() {
        if(areEntitiesValid()){
            if(NUM_RUNS != count){
                Location nextLocation = target.getLocation().clone().add(0, count, 0);
                target.teleport(nextLocation);
                target.playEffect(EntityEffect.HURT);
                double alteredHealth = SpellUtils.alterHealth(target, -(damage / 3));
                SpellUtils.alterHealth(caster, alteredHealth);
                SpellUtils.spawnParticle(target, Particle.HEART, 30, 0.5f, 0.5f, 0.5f);
                SpellUtils.spawnParticle(caster, Particle.VILLAGER_HAPPY, 30, 0.5f, 0.5f, 0.5f);
                count++;
            }else{
                target.setGravity(true);
                PlayerData targetData = Grimmoire.instance.getPlayerDataManager().getData(target.getUniqueId());
                targetData.setFrozen(false);
                targetData.setCanCast(true);
                cancel();
            }
        }
    }

}
