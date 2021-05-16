package net.dohaw.blackclover.grimmoire.spell.type.trap;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.TrapPlayerData;
import net.dohaw.blackclover.util.BlockSnapshot;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class TrapSpell extends CastSpellWrapper implements Listener {

    private int maxNumTraps;
    private Map<UUID, BlockSnapshot> carpetLocationSnapshots = new HashMap<>();

    public TrapSpell(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    @Override
    public void prepareShutdown() {
        for(BlockSnapshot loc : carpetLocationSnapshots.values()){
            loc.apply();
        }
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        if(pd instanceof TrapPlayerData){

            Player player = pd.getPlayer();
            TrapPlayerData tpd = (TrapPlayerData) pd;
            int currentNumTraps = tpd.getNumTraps(getTrapType());
            if(currentNumTraps < maxNumTraps){
                placeTrap(tpd, player.getLocation());
            }else{
                player.sendMessage("You can't place anymore traps right now!");
            }

        }else{
            throw new UnexpectedPlayerData();
        }

        return false;

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.maxNumTraps = grimmoireConfig.getIntegerSetting(KEY, "Max Number of Traps");
    }

    /**
     * Returns the location at which the trap will be placed.
     * This method is important because we alter the placement location in the Reflection spell.
     * @param location Normally the player's location
     */
    public Location getPlacementLocation(Location location){
        return location;
    }

    public void placeTrap(TrapPlayerData placer, Location location){

        Block blockUnderPlayer = getPlacementLocation(location).getBlock();
        Location blockLocation = blockUnderPlayer.getLocation();
        Location centeredLocation = new Location(location.getWorld(), blockLocation.getBlockX() + 0.5D, blockLocation.getBlockY(), blockLocation.getBlockZ() + 0.5D);
        Block locationBlock = centeredLocation.getBlock();

        Trap trap = new Trap(this, BlockSnapshot.toSnapshot(locationBlock), placer.getUuid(), centeredLocation);
        locationBlock.setType(getCarpetMaterial());
        SpellUtils.spawnParticle(location, placeParticles(), 30, 1, 1, 1);
        SpellUtils.playSound(location, Sound.BLOCK_WOOL_PLACE);
        placer.addTrap(trap);

    }

    public abstract void onStepOnTrap(Trap trap, LivingEntity trapStepper);

    public abstract Material getCarpetMaterial();

    public abstract Particle placeParticles();

    public abstract TrapType getTrapType();

}
