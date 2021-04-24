package net.dohaw.blackclover.grimmoire.spell.wind;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Collection;

// TODO: Add vertical knockback
public class Push extends CastSpellWrapper {

    private double forceMultiplier;
    private double radius;

    public Push(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PUSH, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        SpellUtils.spawnParticle(player, Particle.END_ROD, 30, 1, 1, 1);
        SpellUtils.spawnParticle(player, Particle.SPELL_INSTANT, 30, 1, 1, 1);
        SpellUtils.playSound(player, Sound.BLOCK_BAMBOO_BREAK);
        Collection<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
        for(Entity en : nearbyEntities){
            if(en instanceof LivingEntity){
                LivingEntity livingEntity = (LivingEntity) en;
                pushAway(livingEntity, player.getLocation());
            }
        }

        return true;
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getDoubleSetting(KEY, "Radius");
        this.forceMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Force Multiplier");
    }

    public void pushAway(LivingEntity willBePushed, Location pushFrom) {
        willBePushed.setVelocity(pushFrom.toVector().subtract(willBePushed.getLocation().toVector()).normalize().multiply(-forceMultiplier));
    }

}
