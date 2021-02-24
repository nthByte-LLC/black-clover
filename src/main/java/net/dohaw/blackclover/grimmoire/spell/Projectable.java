package net.dohaw.blackclover.grimmoire.spell;

import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface Projectable {
    void onHit(EntityDamageByEntityEvent event, Entity eDamaged, PlayerData pdDamager);
}
