package net.dohaw.blackclover.grimmoire.spell;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

//TODO: Refactor code to where all the spells that launch projectiles use this class.
public abstract class ProjectileSpellWrapper extends CastSpellWrapper implements Listener {

    private double damage;
    private double forceMultiplier;

    public ProjectileSpellWrapper(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.forceMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Force Multiplier");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        Player player = pd.getPlayer();
        Projectile proj = SpellUtils.fireProjectile(player, this, getProjectileMaterial());
        proj.setVelocity(proj.getVelocity().multiply(forceMultiplier));
        onProjectileLaunch(player);
        return true;
    }

    @EventHandler
    public void onProjectileHit(SpellDamageEvent e){
        if(e.getSpell() == KEY){
            if(!e.isCancelled()){
                // I know this is always a living entity. I need to refactor the SpellDamageEvent to where it holds a LivingEntity instead of a Entity.
                // It's a lot to refactor at the moment
                // TODO: Refactor SpellDamageEvent to hold LivingEntity instead of an Entity object
                onProjectileHit((LivingEntity) e.getDamaged());
                e.setDamage(damage);
            }
        }
    }

    public abstract void onProjectileLaunch(LivingEntity projectileLauncher);

    public abstract void onProjectileHit(LivingEntity entityHit);

    public abstract Material getProjectileMaterial();

}
