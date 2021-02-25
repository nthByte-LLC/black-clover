package net.dohaw.blackclover.grimmoire.spell;

import net.dohaw.blackclover.config.GrimmoireConfig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class PassiveSpellWrapper extends SpellWrapper implements Listener {

    public PassiveSpellWrapper(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageByEntityEvent e){
        Entity eDamager = e.getDamager();
        if(eDamager instanceof Projectile){

        }
    }

}
