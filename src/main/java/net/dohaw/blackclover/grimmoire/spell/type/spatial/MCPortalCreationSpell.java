package net.dohaw.blackclover.grimmoire.spell.type.spatial;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.PersistableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

/**
 * Defines a spell that creates either a nether portal or a end portal
 */
public abstract class MCPortalCreationSpell extends CastSpellWrapper implements Listener, PersistableSpell {

    protected Map<UUID, List<List<Block>>> portals = new HashMap<>();

    protected Material material;
    protected int horizontalLength = 4, verticalLength = 5;

    public MCPortalCreationSpell(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
        this.material = spellType == SpellType.HELL_GATE ? Material.OBSIDIAN : Material.END_PORTAL_FRAME;
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();

        Location locationInFront = LocationUtil.getAbsoluteLocationInFront(player, 2);
        String playerFacingStr = player.getFacing().toString().toLowerCase();

        // Sets the direction to a proper direction so that we don't get weird portals getting created due to the nature of LocationUtil methods
        if(playerFacingStr.contains("east")){
            locationInFront.setYaw(270);
        }else if(playerFacingStr.contains("west")){
            locationInFront.setYaw(90);
        }else if(playerFacingStr.contains("south")){
            locationInFront.setYaw(0);
        }else{
            locationInFront.setYaw(180);
        }

        Location bottomLeftCorner = LocationUtil.getAbsoluteLocationToLeft(locationInFront, 1);

        List<List<Block>> portalBlocks = makePortal(bottomLeftCorner);

        /*
            Either places the end portal blocks or it lights the nether portal on fire.
            It'll be null if it's making a nether portal
         */
        List<Block> portalEntranceBlocks = makePortalEntrance(bottomLeftCorner);
        if(portalEntranceBlocks != null){
            portalBlocks.add(portalEntranceBlocks);
        }
        /*
            #placeHorizontalBlocks and #placeVerticalBlocks returns null if it encounters that a block isn't air.
            Sets the placed blocks back to air if there isn't sufficient space for all the blocks
         */
        if(portalBlocks.contains(null)){

            for(List<Block> blocks : portalBlocks){
                if(blocks != null){
                    for(Block block : blocks){
                        block.setType(Material.AIR);
                    }
                }
            }

            player.sendMessage("There isn't enough space to make a portal here!");

            return false;

        }
        removePreviousPortal(player);

        portals.put(player.getUniqueId(), portalBlocks);
        SpellUtils.playSound(player, getPortalCreationSound());

        return true;

    }

    @Override
    public void prepareShutdown() {
        for(List<List<Block>> portalBlocks : portals.values()){
            for(List<Block> blocks : portalBlocks){
                removePortalBlocks(blocks);
            }
        }
    }

    protected void removePortalBlocks(List<Block> portalBlocks){
        portalBlocks.forEach(block -> block.setType(Material.AIR));
    }

    protected void removePreviousPortal(Player player){
        UUID playerUUID = player.getUniqueId();
        if(portals.containsKey(playerUUID)){
            portals.remove(playerUUID).forEach(this::removePortalBlocks);
        }
    }

    public List<Block> placeHorizontalBlocks(Location startLocation){

        List<Block> blocksPlaced = new ArrayList<>();
        Location currentLocation = startLocation.clone();
        for(int i = 0; i < horizontalLength; i++){

            Block currentBlock = currentLocation.getBlock();
            Material currentBlockType = currentBlock.getType();
            if(currentBlockType != Material.AIR && currentBlockType != Material.OBSIDIAN && currentBlockType != Material.END_PORTAL_FRAME){
                return null;
            }
            currentLocation.getBlock().setType(material);
            blocksPlaced.add(currentBlock);
            currentLocation = LocationUtil.getAbsoluteLocationToRight(currentLocation.clone(), 1);

        }

        return blocksPlaced;

    }

    public List<Block> placeVerticalBlocks(Location startLocation){

        List<Block> blocksPlaced = new ArrayList<>();
        Location currentLocation = startLocation.clone();
        for(int i = 0; i < verticalLength; i++){

            Block currentBlock = currentLocation.getBlock();
            Material currentBlockType = currentBlock.getType();
            if(currentBlockType != Material.AIR && currentBlockType != Material.OBSIDIAN && currentBlockType != Material.END_PORTAL_FRAME){
                return null;
            }

            currentLocation.getBlock().setType(material);
            blocksPlaced.add(currentBlock);
            if(KEY == SpellType.HELL_GATE){
                currentLocation = currentLocation.clone().add(0, 1, 0);
            }else{
                currentLocation = LocationUtil.getAbsoluteLocationInFront(currentLocation.clone(), 1);
            }

        }

        return blocksPlaced;

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        removePreviousPortal(player);
    }

    public abstract List<List<Block>> makePortal(Location startLocation);

    public abstract List<Block> makePortalEntrance(Location bottomLeftCorner);

    public abstract Sound getPortalCreationSound();

}
