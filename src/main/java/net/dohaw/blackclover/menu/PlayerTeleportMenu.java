package net.dohaw.blackclover.menu;

import net.dohaw.blackclover.event.PostCastSpellEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.type.spatial.PlayerPortal;
import net.dohaw.blackclover.grimmoire.spell.type.spatial.Portal;
import net.dohaw.blackclover.grimmoire.spell.type.spatial.Teleport;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.StringUtils;
import net.dohaw.corelib.helpers.ItemStackHelper;
import net.dohaw.corelib.menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * A menu that shows all the player's in the game. You can click on their head and it'll make a portal that'll teleport you to them.
 */
public class PlayerTeleportMenu extends Menu implements Listener {

    private Teleport spell;

    public PlayerTeleportMenu(JavaPlugin plugin, Teleport spell) {
        super(plugin, null, "Teleport To A Player", 54);
        this.spell = spell;
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player p) {

        for(Player player : Bukkit.getOnlinePlayers()){
            if(!player.getUniqueId().equals(p.getUniqueId())){
                ItemStack playerHead = ItemStackHelper.getPlayerHead(player.getUniqueId());
                inv.addItem(createGuiItem(playerHead, "&a&l" + player.getName(), Collections.singletonList("&fTeleport to this player")));
            }
        }

        this.fillerMat = Material.BLACK_STAINED_GLASS_PANE;
        fillMenu(false);

    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        Inventory clickedInventory = e.getClickedInventory();
        Inventory topInventory = player.getOpenInventory().getTopInventory();

        if (clickedInventory == null) return;
        if (!topInventory.equals(inv) || !clickedInventory.equals(topInventory)) return;
        if (clickedItem == null && e.getCursor() == null) return;

        e.setCancelled(true);

        if(clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD){

            player.closeInventory();

            String clickedPlayerName = StringUtils.removeChatColor(clickedItem.getItemMeta().getDisplayName());
            Player clickedPlayer = Bukkit.getPlayer(clickedPlayerName);

            if(clickedPlayer == null || !clickedPlayer.isValid()){
                player.sendMessage("This player is either offline or dead...");
                return;
            }

            Map<UUID, Portal> portals = spell.getPortals();
            Portal potentialPreviousPortal = portals.remove(player.getUniqueId());
            if(potentialPreviousPortal != null){
                potentialPreviousPortal.getPortalEnterChecker().cancel();
                potentialPreviousPortal.getPortalDrawer().cancel();
            }

            Location portalStartLocation = spell.getPortalStartLocation(player);
            portals.put(player.getUniqueId(), new PlayerPortal(portalStartLocation, spell, clickedPlayer));

            PlayerData playerData = Grimmoire.instance.getPlayerDataManager().getData(player);
            spell.deductMana(playerData);
            // Starts cooldown when you actually start teleporting
            Bukkit.getPluginManager().callEvent(new PostCastSpellEvent(playerData, spell, true));

        }

    }

}
