package net.dohaw.blackclover.grimmoire.spell.type.time;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Collection;

public class StopTime extends CastSpellWrapper {

    private double radius, durationFrozen;

    public StopTime(GrimmoireConfig grimmoireConfig) {
        super(SpellType.STOP_TIME, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Collection<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
        for(Entity en : nearbyEntities){
            SpellUtils.freezeEntity(en, durationFrozen);
            SpellUtils.spawnParticle(en, Particle.VILLAGER_ANGRY, 30, 1, 1, 1);
        }

        SpellUtils.playSound(player, Sound.BLOCK_ANVIL_PLACE);

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getDoubleSetting(KEY, "Radius");
        this.durationFrozen = grimmoireConfig.getDoubleSetting(KEY, "Duration Frozen");
    }

}
