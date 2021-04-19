package net.dohaw.blackclover.grimmoire.spell.type.fungus;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.type.Fungus;
import net.dohaw.blackclover.menu.FungusMorphMenu;
import net.dohaw.blackclover.playerdata.FungusPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Allows the player to morph into a mushroom
 */
public class Morph extends CastSpellWrapper implements Listener {

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
                    stopMorphing(fpd);
                    System.out.println("IS SNEAKING");
                }else{
                    System.out.println("BRUH");
                    player.sendMessage("You are already morphed!");
                }
                return false;
            }else{
                if(isSneaking){
                    player.sendMessage("You aren't morphed right now!");
                }else{
                    FungusMorphMenu fungusMorphMenu = new FungusMorphMenu(Grimmoire.instance);
                    fungusMorphMenu.initializeItems(player);
                    fungusMorphMenu.openInventory(player);
                }
            }

        }else{
            throw new UnexpectedPlayerData();
        }

        return false;

    }

    @Override
    public void prepareShutdown() {

    }

    /*
        Cancels the picking up of items if they are morphed.
     */
    @EventHandler
    public void onPlayerPickupItems(EntityPickupItemEvent e){
        Entity entity = e.getEntity();
        if(entity instanceof Player){
            Player player = (Player) entity;
            PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
            if(pd.getGrimmoireWrapper().getKEY() == GrimmoireType.FUNGUS){
                FungusPlayerData fpd = (FungusPlayerData) pd;
                if(fpd.isMorphed()){
                    e.setCancelled(true);
                }
            }
        }
    }


    private void stopMorphing(FungusPlayerData fpd){

        Player player = fpd.getPlayer();
        player.setInvisible(false);
        fpd.setFrozen(false);

        ItemStack[] contents = fpd.getItemsBeforeMorphing();
        player.getInventory().setContents(contents);

    }

}
