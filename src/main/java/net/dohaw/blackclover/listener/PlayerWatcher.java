package net.dohaw.blackclover.listener;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.util.NBTHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerWatcher implements Listener {

    private BlackCloverPlugin plugin;

    public PlayerWatcher(BlackCloverPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        if(!NBTHandler.hasGrimmoire(e.getPlayer())){

        }else{

        }

    }

    @EventHandler
    public void onCastSpell(PlayerInteractEvent e){



    }

    private ItemStack ensureProperHotbar(){



    }

    private ItemStack giveRandomGrimmoire(){

    }

}
