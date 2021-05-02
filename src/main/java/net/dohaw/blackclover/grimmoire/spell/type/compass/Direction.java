package net.dohaw.blackclover.grimmoire.spell.type.compass;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.HelixParticleRunner;
import net.dohaw.blackclover.util.BlockSnapshot;
import net.dohaw.blackclover.util.LocationUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Direction extends CastSpellWrapper {

    private List<Location> woolLocations = new ArrayList<>();
    private double woolStayTime;

    public Direction(GrimmoireConfig grimmoireConfig) {
        super(SpellType.DIRECTION, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Location bedLocation = player.getBedSpawnLocation();
        if(bedLocation != null){

            Location playerLocation = player.getLocation();
            Location woolLocation = LocationUtil.getLocationInDirection(playerLocation, bedLocation, 2).add(0, 0.5, 0);

            BlockSnapshot woolPreviousBlock = BlockSnapshot.toSnapshot(woolLocation.getBlock());
            woolLocation.getBlock().setType(Material.BLUE_WOOL);

            HelixParticleRunner helix = new HelixParticleRunner(woolLocation, new Particle.DustOptions(Color.WHITE, 3), 1.25, true);
            helix.runTaskTimer(Grimmoire.instance, 0L, 3L);

            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                woolPreviousBlock.apply();
                helix.cancel();
            }, (long) (woolStayTime * 20));

            return true;

        }else{
           player.sendMessage("You do not have a bed that you have slept in!");
        }

        return false;
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.woolStayTime = grimmoireConfig.getDoubleSetting(KEY, "Wool Stay Time");
    }

}
