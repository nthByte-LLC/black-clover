package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.DamageSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.Projectable;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftSnowball;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Listener;
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
        ((CraftSnowball) projectile).getHandle().setItem(CraftItemStack.asNMSCopy(new ItemStack(Material.FIRE)));
        pd.setManaAmount((int) (pd.getManaAmount() - regenConsumed));
    }

    @Override
    public void onHit(Entity eDamaged, PlayerData pdDamager) {

    }

}
