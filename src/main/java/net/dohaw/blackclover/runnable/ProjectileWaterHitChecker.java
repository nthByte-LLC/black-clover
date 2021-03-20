package net.dohaw.blackclover.runnable;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

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
                if(currentBlock.hasMetadata("water-wall")){
                    projectile.remove();
                }
            }
        }
    }

}
