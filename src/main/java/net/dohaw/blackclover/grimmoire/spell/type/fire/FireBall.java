package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DamageableSpell;
import net.dohaw.blackclover.grimmoire.spell.Projectable;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftSnowball;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class FireBall extends CastSpellWrapper implements Listener, Projectable, DamageableSpell {

    public FireBall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_BALL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        SpellUtils.fireProjectile(player, this, Material.FIRE_CHARGE);
        pd.setManaAmount((int) (pd.getManaAmount() - regenConsumed));
        return true;
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e, Entity eDamaged, PlayerData pdDamager) {

        SpellDamageEvent event = new SpellDamageEvent(KEY, eDamaged, pdDamager.getPlayer());
        Bukkit.getPluginManager().callEvent(event);

        if(!event.isCancelled()){
            double initDmg = e.getDamage();
            /*
                Will be 0 if i'm masking a snowball as a projectile...
             */
            if (initDmg == 0) {
                initDmg = 1;
            }

            double finalDmg = initDmg * damageScale;
            e.setDamage(finalDmg);

            eDamaged.getWorld().spawnParticle(particle, eDamaged.getLocation(), 30, 1, 1, 1);
            eDamaged.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, eDamaged.getLocation(), 30, 1, 1, 1);
        }


    }

}
