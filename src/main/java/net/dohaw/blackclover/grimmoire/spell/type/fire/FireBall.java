package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.DamageSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.Projectable;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.PDCHandler;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftSnowball;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class FireBall extends DamageSpellWrapper implements Listener, Projectable {

    public FireBall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_BALL, grimmoireConfig);
    }

    @Override
    public void cast(PlayerData pd) {
        Player player = pd.getPlayer();
        CraftLivingEntity cPlayer = (CraftLivingEntity) player;
        Projectile projectile = cPlayer.launchProjectile(Snowball.class);
        this.markAsSpellBinding(projectile);
        ((CraftSnowball) projectile).getHandle().setItem(CraftItemStack.asNMSCopy(new ItemStack(Material.FIRE_CHARGE)));
        pd.setManaAmount((int) (pd.getManaAmount() - regenConsumed));
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Entity eDamaged, PlayerData pdDamager) {

        double initDmg = e.getDamage();
        /*
            Will be 0 if i'm masking a snowball as a projectile...
         */
        if(initDmg == 0){
            initDmg = 1;
        }

        double finalDmg = initDmg * damageScale;
        e.setDamage(finalDmg);

        eDamaged.getWorld().spawnParticle(particle, eDamaged.getLocation(), 30, 1, 1, 1);
        eDamaged.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, eDamaged.getLocation(), 30, 1, 1, 1);

    }

}
