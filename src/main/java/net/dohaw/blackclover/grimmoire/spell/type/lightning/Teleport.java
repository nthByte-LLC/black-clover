package net.dohaw.blackclover.grimmoire.spell.type.lightning;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Teleport extends CastSpellWrapper {

    private int castDistance;

    public Teleport(GrimmoireConfig grimmoireConfig) {
        super(SpellType.TELEPORT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, entityInSight)){

            Location locationBehind = LocationUtil.getAbsoluteLocationInBack(entityInSight.getLocation(), 1);
            SpellUtils.spawnParticle(player, Particle.SQUID_INK, 30, 1, 1, 1);
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                player.teleport(locationBehind);
                SpellUtils.playSound(player, Sound.ITEM_CHORUS_FRUIT_TELEPORT);
            }, 3);
            return true;

        }

        return false;
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }
}
