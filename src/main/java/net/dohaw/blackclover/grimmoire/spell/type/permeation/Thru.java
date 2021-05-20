package net.dohaw.blackclover.grimmoire.spell.type.permeation;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Thru extends CastSpellWrapper {

    private int maxCastDistance;

    public Thru(GrimmoireConfig grimmoireConfig) {
        super(SpellType.THRU, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();
        Block blockTarget = null;
        int numIterations = 0;
        for(int i = 0; i < maxCastDistance; i++){
            Location currentLocation = LocationUtil.getLocationInFront(player, i);
            Block currentLocationBlock = currentLocation.getBlock();
            if(!currentLocationBlock.getType().isAir()){
                blockTarget = currentLocationBlock;
                numIterations = i;
                break;
            }
        }

        if(blockTarget == null){
            player.sendMessage("You aren't targeting any block within a reasonable distance!");
            return false;
        }

        Block inFrontOfTarget = LocationUtil.getLocationInFront(player, numIterations + 1).getBlock();
        Block headLevelBlock = inFrontOfTarget.getRelative(BlockFace.UP);

        inFrontOfTarget.setType(Material.REDSTONE_BLOCK);

        if(!inFrontOfTarget.getType().isAir() || !headLevelBlock.getType().isAir()){
            player.sendMessage("There isn't sufficient space!");
            return false;
        }

        SpellUtils.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT);
        SpellUtils.spawnParticle(player, Particle.CLOUD, 30, 1, 1, 1);
        player.teleport(inFrontOfTarget.getLocation());

        return true;
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.maxCastDistance = grimmoireConfig.getIntegerSetting(KEY, "Max Cast Distance");
    }

    private Block getTargetBlock(Player player){

        return null;
    }

}
