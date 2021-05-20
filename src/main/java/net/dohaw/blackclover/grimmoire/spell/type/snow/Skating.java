package net.dohaw.blackclover.grimmoire.spell.type.snow;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.SnowPlayerData;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Skating extends CastSpellWrapper implements Listener {

    private double duration;

    public Skating(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SKATING, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd)  {
        if(pd instanceof SnowPlayerData){
            Player player = pd.getPlayer();
            SnowPlayerData spd = (SnowPlayerData) pd;
            if(!spd.isSkating()){
                spd.setSkating(true);
                SpellUtils.spawnParticle(player, Particle.SNOW_SHOVEL, 20, 1, 1, 1);
                SpellUtils.playSound(player, Sound.BLOCK_SNOW_STEP);
                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, spd::stopSkating, (long) (duration * 20));
            }else{
                player.sendMessage("You are already skating!");
                return false;
            }
        }else{
            try {
                throw new UnexpectedPlayerData();
            } catch (UnexpectedPlayerData unexpectedPlayerData) {
                unexpectedPlayerData.printStackTrace();
            }
        }
        return true;
    }

    @EventHandler
    public void onSkate(PlayerMoveEvent e){

        Player player = e.getPlayer();
        PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
        if(LocationUtil.hasMoved(e.getTo(), e.getFrom(), true)){
            if(pd.getGrimmoireWrapper().getKEY() == GrimmoireType.SNOW){

                SnowPlayerData spd = (SnowPlayerData) pd;
                if(spd.isSkating()){

                    Location playerLoc = player.getLocation().add(0, -1, 0);
                    Block playerLocBlock = playerLoc.getBlock();
                    if(!spd.hasSkatedOverBlock(playerLocBlock)){
                        if(playerLocBlock.getType() != Material.ICE){
                            spd.addBlockSkatedOver(playerLocBlock);
                            playerLocBlock.setType(Material.ICE);
                        }
                    }

                }

            }
        }

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

}
