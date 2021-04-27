package net.dohaw.blackclover.grimmoire.spell.type.time;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Regeneration extends CastSpellWrapper implements Listener {

    public Regeneration(GrimmoireConfig grimmoireConfig) {
        super(SpellType.REGENERATION, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        healToFull(pd);
        return true;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerDeath(EntityDamageEvent e){

        Entity eDamaged = e.getEntity();
        if(eDamaged instanceof Player){

            Player damaged = (Player) eDamaged;
            PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(damaged);
            if(pd.getGrimmoireType() == GrimmoireType.TIME && !pd.isSpellOnCooldown(KEY)){
                // They are about to die
                if(damaged.getHealth() - e.getFinalDamage() <= 0){
                    healToFull(pd);
                    e.setCancelled(true);
                }

            }

        }

    }

    private void healToFull(PlayerData data){

        Player player = data.getPlayer();
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        player.setHealth(maxHealth);
        SpellUtils.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        SpellUtils.spawnParticle(player, Particle.TOTEM, 30, 1, 1, 1);

        // Sets them to 10% regen
        int maxRegen = data.getMaxRegen();
        int regen = (int) (maxRegen * .1);
        data.setRegenAmount(regen);

    }

    @Override
    public void prepareShutdown() {

    }
}
