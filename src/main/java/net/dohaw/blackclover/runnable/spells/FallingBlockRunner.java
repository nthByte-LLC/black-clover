package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;

public class FallingBlockRunner extends BukkitRunnable {

    private HashSet<Entity> hurtEntities = new HashSet<>();
    private Player caster;
    private FallingBlock block;
    private double damage;
    private SpellType spell;
    private BukkitTask intertiaEnforcer;

    public FallingBlockRunner(Player caster, FallingBlock block, SpellType spell, double damage, boolean hasIntertia){
        this.caster = caster;
        this.block = block;
        this.damage = damage;
        this.spell = spell;
        if(hasIntertia) {
            this.intertiaEnforcer = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
                for (Entity e : hurtEntities) {
                    e.teleport(block);
                }
            }, 0L, 1L);
        }
    }

    @Override
    public void run() {

        if(!block.isOnGround()){
            for(Entity e : block.getNearbyEntities(0.5, 0.5, 0.5)){
                if(e instanceof LivingEntity && !hurtEntities.contains(e) && !caster.getUniqueId().equals(e.getUniqueId())){
                    LivingEntity le = (LivingEntity) e;
                    boolean isDamaged = SpellUtils.damageEntity(le, caster, spell, damage);
                    if(isDamaged){
                        hurtEntities.add(e);
                        SpellUtils.spawnParticle(e, Particle.END_ROD, 30, 0.1f, 0.1f, 0.1f);
                        SpellUtils.playSound(e, Sound.BLOCK_ROOTS_HIT);
                    }
                }
            }
        }else{
            cancel();
        }

    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        intertiaEnforcer.cancel();
        block.remove();
        block.getLocation().getBlock().setType(Material.AIR);
        super.cancel();
    }
}
