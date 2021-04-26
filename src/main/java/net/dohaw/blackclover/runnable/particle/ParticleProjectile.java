package net.dohaw.blackclover.runnable.particle;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.SpellUtils;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftSnowball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class ParticleProjectile extends BukkitRunnable {

    private BukkitTask velocityChanger;


    private double forceMultiplier;
    private Projectile projectile;

    public ParticleProjectile(Player caster, double forceMultiplier){
        this.forceMultiplier = forceMultiplier;
        initProjectile(caster);
    }

    @Override
    public void run() {

        if(projectile.isValid()){
            if(!projectile.isOnGround()){

                double y = projectile.getLocation().getY();
                if(y >= 256){
                    cancel();
                }
                SpellUtils.spawnParticle(projectile.getLocation(), Particle.REDSTONE, new Particle.DustOptions(BukkitColor.PALE_CYAN, 2), 30, 0, 0, 0);

            }else{
                cancel();
            }
        }else{
            cancel();
        }

    }

    private void initProjectile(Player caster){

        this.projectile = caster.launchProjectile(Snowball.class);
        projectile.setGravity(false);

        Grimmoire.LIGHTNING.electricBall.markAsSpellBinding(projectile);

        for(Player player : Bukkit.getOnlinePlayers()){
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(((CraftSnowball) projectile).getHandle().getId()));
        }

        this.velocityChanger = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            Vector velocity = projectile.getVelocity();
            // constantly keeps it moving
            projectile.setVelocity(velocity.multiply(1.05));
        }, 0L, 10L);

    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        System.out.println("CANCELING");
        super.cancel();
        velocityChanger.cancel();
        projectile.remove();
    }
}
