package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FireBall extends CastSpellWrapper implements Listener {

    private double damageScale;

    public FireBall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_BALL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        SpellUtils.fireProjectile(player, this, Material.FIRE_CHARGE);
        return true;
    }

    @EventHandler
    public void onFireBallHit(SpellDamageEvent e){

        if(e.getSpell() == SpellType.FIRE_BALL){

            Entity eDamaged = e.getDamaged();
            double initDmg = e.getDamage();
        /*
            Will be 0 if i'm masking a snowball as a projectile...
         */
            if (initDmg == 0) {
                initDmg = 1;
            }

            double finalDmg = initDmg * damageScale;

            if(!e.isCancelled()){
                e.setDamage(finalDmg);
                eDamaged.getWorld().spawnParticle(particle, eDamaged.getLocation(), 30, 1, 1, 1);
                eDamaged.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, eDamaged.getLocation(), 30, 1, 1, 1);
            }

        }


    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.damageScale = grimmoireConfig.getDoubleSetting(KEY, "Damage Scale");
    }

}
