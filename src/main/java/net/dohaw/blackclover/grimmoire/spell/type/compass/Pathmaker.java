package net.dohaw.blackclover.grimmoire.spell.type.compass;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.CompassPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.Event;

import java.util.*;

/**
 * Makes a path of particles wherever you walk.
 */
public class Pathmaker extends ActivatableSpellWrapper {

    private int maxPathPoints;

    private Map<UUID, LinkedList<Location>> allPaths = new HashMap<>();

    public Pathmaker(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PATH_MAKER, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData{

        if(pd instanceof CompassPlayerData){
            CompassPlayerData cpd = (CompassPlayerData) pd;
            cpd.setMakingPath(true);
            return super.cast(e, pd);
        }else{
            throw new UnexpectedPlayerData();
        }

    }

    @Override
    public void doRunnableTick(PlayerData caster) {

        CompassPlayerData cpd = (CompassPlayerData) caster;
        LinkedList<Location> path = cpd.getPath();
        // Removes the oldest path point
        if(path.size() == maxPathPoints){
            path.removeFirst();
        }

        path.add(caster.getPlayer().getLocation());
        allPaths.put(caster.getUUID(), path);

        //Spawn path particles
        for(Location loc : path){
            SpellUtils.spawnParticle(loc, Particle.REDSTONE, new Particle.DustOptions(Color.WHITE, 2), 20, 0.2f, 0.2f, 0.2f);
        }

    }

    @Override
    public void deactiveSpell(PlayerData caster)  {
        CompassPlayerData cpd = (CompassPlayerData) caster;
        cpd.setMakingPath(false);
        cpd.getPath().clear();
        allPaths.remove(cpd.getUUID());
    }

    @Override
    public void prepareShutdown() { }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.maxPathPoints = grimmoireConfig.getIntegerSetting(KEY, "Maximum Path Points");
    }

    public Map<UUID, LinkedList<Location>> getAllPaths() {
        return allPaths;
    }

    @Override
    public long getRunnableInterval() {
        return 10;
    }
}
