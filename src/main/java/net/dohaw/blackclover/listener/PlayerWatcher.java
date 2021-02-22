package net.dohaw.blackclover.listener;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.event.PlayerCastSpellEvent;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.PlayerDataManager;
import net.dohaw.blackclover.util.PDCHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerWatcher implements Listener {

    private BlackCloverPlugin plugin;

    public PlayerWatcher(BlackCloverPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        plugin.getPlayerDataManager().loadData(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        PlayerDataManager pdm = plugin.getPlayerDataManager();
        UUID uuid = e.getPlayer().getUniqueId();
        pdm.saveData(uuid);
        pdm.removeDataFromMemory(uuid);
    }

    @EventHandler
    public void onPrepareToCast(PlayerInteractEvent e){

        Action action = e.getAction();
        if(action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR){
            ItemStack item = e.getItem();
            PlayerData pd = plugin.getPlayerDataManager().getData(e.getPlayer().getUniqueId());
            Player player = pd.getPlayer();
            SpellWrapper spellBoundToItem = PDCHandler.getSpellBoundToItem(pd, item);
            if(spellBoundToItem != null){
                e.setCancelled(true);
                if(!pd.getSpellsOnCooldown().contains(spellBoundToItem.getKEY())){
                    if(pd.hasSufficientManaForSpell(spellBoundToItem)){
                        spellBoundToItem.cast(pd);
                        Bukkit.getPluginManager().callEvent(new PlayerCastSpellEvent(pd, spellBoundToItem));
                    }else{
                        player.sendMessage("You don't have enough mana at the moment!");
                    }
                }else{
                    player.sendMessage("This spell is on cooldown!");
                }
            }else{
                System.out.println("NOT SPELL BOUND");
            }
        }


    }

    @EventHandler
    public void onPostCast(PlayerCastSpellEvent e){
        PlayerData pd = e.getPlayerData();
        SpellWrapper spellCasted = e.getSpellCasted();
        pd.getSpellsOnCooldown().add(spellCasted.getKEY());
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            pd.getSpellsOnCooldown().remove(spellCasted.getKEY());
            Bukkit.broadcastMessage("NOT ON COOLDOWN ANYMORE");
        }, (long) (spellCasted.getCooldown() * 20));
    }

    private ItemStack ensureProperHotbar(){

        return null;

    }

}
