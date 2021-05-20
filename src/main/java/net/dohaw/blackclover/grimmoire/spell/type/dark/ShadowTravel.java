package net.dohaw.blackclover.grimmoire.spell.type.dark;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ShadowTravel extends CastSpellWrapper {

    private int maxDistanceTravel;

    public ShadowTravel(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SHADOW_TRAVEL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player casterPlayer = pd.getPlayer();
        Block targetBlock = casterPlayer.getTargetBlockExact(maxDistanceTravel);
        if(targetBlock != null){

            Block blockAboveTargetBlock = targetBlock.getRelative(BlockFace.UP);
            if(blockAboveTargetBlock.getType().isAir()){

                SpellUtils.spawnParticle(casterPlayer.getLocation(), Particle.REDSTONE, new Particle.DustOptions(Color.BLACK, 2), 30, 1, 1, 1);
                SpellUtils.playSound(casterPlayer, Sound.ITEM_CHORUS_FRUIT_TELEPORT);

                Location teleportLocation = blockAboveTargetBlock.getLocation();
                casterPlayer.teleport(teleportLocation);
                SpellUtils.spawnParticle(casterPlayer.getLocation(), Particle.REDSTONE, new Particle.DustOptions(Color.BLACK, 2), 30, 1, 1, 1);

                return true;

            }else{
                casterPlayer.sendMessage("There is a block above your target block!");
            }

        }else{
            casterPlayer.sendMessage("There isn't a block within a reasonable distance from you!");
        }


        return false;

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.maxDistanceTravel = grimmoireConfig.getIntegerSetting(KEY, "Max Distance Travel");
    }

}
