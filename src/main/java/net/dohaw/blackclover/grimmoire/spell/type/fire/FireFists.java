package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.PlayerCastSpellEvent;
import net.dohaw.blackclover.grimmoire.spell.DamageSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireFists extends DamageSpellWrapper implements Listener {

    public FireFists(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_FISTS, grimmoireConfig);
    }

    @Override
    public void cast(PlayerData pd) {
        Bukkit.broadcastMessage("CASTING FIRE FISTS");
    }

    @EventHandler
    public void onPlayerStrike(EntityDamageByEntityEvent e){

        Entity eDamager = e.getDamager();
        if(eDamager instanceof Player){
            Player player = (Player) eDamager;
        }

    }

}
