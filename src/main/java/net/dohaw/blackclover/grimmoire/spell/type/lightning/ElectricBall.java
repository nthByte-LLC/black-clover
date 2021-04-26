package net.dohaw.blackclover.grimmoire.spell.type.lightning;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.ParticleProjectile;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Shoots a electric ball from the player, sort of like a fireball.
 */
public class ElectricBall extends CastSpellWrapper implements Listener {

    private double damage;

    public ElectricBall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ELECTRIC_BALL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        new ParticleProjectile(player, 1).runTaskTimer(Grimmoire.instance, 0L, 1L);

        return true;
    }

    @EventHandler
    public void onProjectileHit(SpellDamageEvent e){
        if(!e.isCancelled()){
            if(e.getSpell() == KEY){
                Entity eDamaged = e.getDamaged();
                SpellUtils.spawnParticle(eDamaged, Particle.SOUL_FIRE_FLAME, 30, 0, 0, 0);
                SpellUtils.playSound(eDamaged, Sound.ENTITY_GENERIC_EXTINGUISH_FIRE);
                e.setDamage(damage);
            }
        }
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
    }
}
