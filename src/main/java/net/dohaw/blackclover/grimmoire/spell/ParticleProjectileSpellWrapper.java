package net.dohaw.blackclover.grimmoire.spell;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.ParticleProjectile;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * A casted spell that involves shooting a particle projectile
 */
public abstract class ParticleProjectileSpellWrapper extends CastSpellWrapper implements Listener {

    private final float PARTICLE_OFFSET = 0;

    private double forceMultiplier;
    private double damage;

    public ParticleProjectileSpellWrapper(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        new ParticleProjectile(pd.getPlayer(), this, getDustOptions(), forceMultiplier).runTaskTimer(Grimmoire.instance, 0L, 2L);
        return true;
    }

    @EventHandler
    public void onProjectileHit(SpellDamageEvent e){
        if(!e.isCancelled()){
            if(e.getSpell() == KEY){
                Entity eDamaged = e.getDamaged();
                SpellUtils.spawnParticle(eDamaged, getOnHitParticle(), 30, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_OFFSET);
                SpellUtils.playSound(eDamaged, getOnHitSound());
                e.setDamage(damage);
            }
        }
    }

    public abstract Particle getOnHitParticle();

    public abstract Sound getOnHitSound();

    public abstract Particle.DustOptions getDustOptions();

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
        this.forceMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Force Multiplier");
    }


}
