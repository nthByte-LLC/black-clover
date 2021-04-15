package net.dohaw.blackclover.grimmoire.spell.type.fungus;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.FungusPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Allows the player to morph into a mushroom
 */
public class Morph extends CastSpellWrapper {

    public Morph(GrimmoireConfig grimmoireConfig) {
        super(SpellType.MORPH, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        if(pd instanceof FungusPlayerData){
            FungusPlayerData fpd = (FungusPlayerData) pd;
            Player player = pd.getPlayer();
            boolean isSneaking = player.isSneaking();
            if(fpd.isMorphed()){
                // They want to un-morph
                if(isSneaking){

                }else{
                    player.sendMessage("You are already morphed!");
                }
                return false;
            }else{
                if(isSneaking){
                    player.sendMessage("You aren't morphed right now!");
                }else{
                    morphPlayer(pd);
                }
            }

        }else{
            throw new UnexpectedPlayerData();
        }

        return false;

    }

    private void morphPlayer(PlayerData data){

        Player player = data.getPlayer();


        World world = player.getWorld();

        boolean isGenerated = world.generateTree(player.getLocation(), TreeType.RED_MUSHROOM);


    }

    @Override
    public void prepareShutdown() {

    }
}
