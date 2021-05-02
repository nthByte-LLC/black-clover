package net.dohaw.blackclover.grimmoire.spell.type.fungus;

import net.dohaw.blackclover.MorphStructureRemovalSession;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.menu.FungusMorphMenu;
import net.dohaw.blackclover.playerdata.FungusPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
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
                }else{
                    player.sendMessage("You are already morphed!");
                }
                return false;
            }else{
                if(isSneaking){
                    player.sendMessage("You aren't morphed right now!");
                }else{
                    Material underPlayerMaterial = player.getLocation().subtract(0, 1, 0).getBlock().getType();
                    boolean isOnGround = underPlayerMaterial.isSolid();
                    if(isOnGround){
                        FungusMorphMenu fungusMorphMenu = new FungusMorphMenu(Grimmoire.instance);
                        fungusMorphMenu.initializeItems(player);
                        fungusMorphMenu.openInventory(player);
                    }else{
                        player.sendMessage("You can only morph on the ground!");
                    }
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
        fpd.setMorphed(false);
        fpd.setCanCast(true);

        ItemStack[] contents = fpd.getItemsBeforeMorphing();
        player.getInventory().setContents(contents);

        TreeType treeType = fpd.getMorphType();
        Location morphLocation = fpd.getMorphLocation();
        MorphStructureRemovalSession session = new MorphStructureRemovalSession(morphLocation, treeType);
        session.startRemovalProcess();

    }

}
