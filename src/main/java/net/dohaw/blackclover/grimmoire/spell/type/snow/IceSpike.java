package net.dohaw.blackclover.grimmoire.spell.type.snow;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class IceSpike extends CastSpellWrapper implements Listener {

    private double velocityScale;
    private double damage;

    public IceSpike(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ICE_SPIKE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Projectile proj = SpellUtils.fireProjectile(player, this, Material.PACKED_ICE);
        Vector velocity = proj.getVelocity();
        System.out.println("SCALE: " + velocityScale);
        proj.setVelocity(velocity.multiply(velocityScale));

        SpellUtils.playSound(player, Sound.ENTITY_SNOW_GOLEM_SHOOT);
        SpellUtils.spawnParticle(player, Particle.EXPLOSION_NORMAL, 20, 1, 1, 1);

        return true;
    }

    @EventHandler
    public void onIceSpikeDamage(SpellDamageEvent e){
        if(e.getSpell() == KEY){
            if(!e.isCancelled()){
                e.setDamage(damage);
            }
        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.velocityScale = grimmoireConfig.getDoubleSetting(KEY, "Velocity Scale");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
    }
}
