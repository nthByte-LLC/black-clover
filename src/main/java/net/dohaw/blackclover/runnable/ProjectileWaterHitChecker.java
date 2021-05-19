package net.dohaw.blackclover.runnable;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

/**
    Checks to see when a projectile hits water. If so, then it checks to see if it's hitting a water wall. If so, then remove it.
 */
public class ProjectileWaterHitChecker extends BukkitRunnable {

    private Projectile projectile;

    public ProjectileWaterHitChecker(Projectile projectile){
        this.projectile = projectile;
    }

    @Override
    public void run() {
        if(projectile.isOnGround()){
            this.cancel();
        }else{
            Block currentBlock = projectile.getLocation().getBlock();
            if(currentBlock.getType() == Material.WATER){
                if(currentBlock.hasMetadata("water-wall-mark")){
                    projectile.remove();
                    this.cancel();
                }
            }
        }
    }

}
