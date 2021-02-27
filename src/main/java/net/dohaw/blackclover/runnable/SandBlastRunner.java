package net.dohaw.blackclover.runnable;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;

public class SandBlastRunner extends BukkitRunnable {

    private HashSet<Entity> hurtEntities = new HashSet<>();
    private Player caster;
    private FallingBlock block;
    private double damageScale;
    private BukkitTask teleporter;

    public SandBlastRunner(Player caster, FallingBlock block, double damageScale){
        this.caster = caster;
        this.block = block;
        this.damageScale = damageScale;
        this.teleporter = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            for(Entity e : hurtEntities){
                e.teleport(block);
            }
        }, 0L, 1L);
    }

    @Override
    public void run() {

        if(!block.isOnGround()){
            for(Entity e : block.getNearbyEntities(1, 1, 1)){
                if(e instanceof LivingEntity && !hurtEntities.contains(e) && !caster.getUniqueId().equals(e.getUniqueId())){
                    hurtEntities.add(e);
                    LivingEntity le = (LivingEntity) e;
                    le.damage(1 * damageScale);
                    SpellUtils.spawnParticle(e, Particle.END_ROD, 30, 0.1f, 0.1f, 0.1f);
                }
            }
        }else{
            teleporter.cancel();
            block.getLocation().getBlock().setType(Material.AIR);
            this.cancel();
        }

    }

}
