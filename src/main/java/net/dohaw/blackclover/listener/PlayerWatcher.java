package net.dohaw.blackclover.listener;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.event.PlayerCastSpellEvent;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.PlayerDataManager;
import net.dohaw.blackclover.util.PDCHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

        ItemStack item = e.getItem();
        PlayerData pd = plugin.getPlayerDataManager().getData(e.getPlayer().getUniqueId());
        SpellWrapper spellBoundToItem = PDCHandler.getSpellBoundToItem(pd, item);
        if(spellBoundToItem != null){
            spellBoundToItem.cast(pd);
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
