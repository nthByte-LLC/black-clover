package net.dohaw.blackclover.grimmoire.spell.type.anti;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.PassiveSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Disable extends PassiveSpellWrapper implements Listener {

    public Disable(GrimmoireConfig grimmoireConfig) {
        super(SpellType.DISABLE, grimmoireConfig);
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e){
        Entity eDamaged = e.getEntity();
        Entity eDamager = e.getDamager();
        if(eDamaged instanceof Player && eDamager instanceof Player){
            Player damaged = (Player) eDamaged;
            Player damager = (Player) eDamager;
            PlayerData damagerData = Grimmoire.instance.getPlayerDataManager().getData(damager.getUniqueId());

        }
    }

}
