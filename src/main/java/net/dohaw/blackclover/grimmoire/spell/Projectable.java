package net.dohaw.blackclover.grimmoire.spell;

import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.entity.Entity;

public interface Projectable {
    void onHit(Entity eDamaged, PlayerData pdDamager);
}
