package net.dohaw.blackclover.menu;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.StringUtils;
import net.dohaw.corelib.menus.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfirmationMenu extends Menu implements Listener {

    private SpellWrapper spellUnlocking;

    public ConfirmationMenu(JavaPlugin plugin, Menu previousMenu, SpellWrapper spellUnlocking) {
        super(plugin, previousMenu, "Confirmation", 27);
        this.spellUnlocking = spellUnlocking;
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player p) {
        inv.setItem(11, createGuiItem(Material.GREEN_CONCRETE, "&a&lConfirm", new ArrayList<>()));
        inv.setItem(15, createGuiItem(Material.RED_CONCRETE, "&c&lCancel", Arrays.asList("&fClicking this takes you back to the", "&fgrimmoire menu")));
        this.fillerMat = Material.BLACK_STAINED_GLASS_PANE;
        fillMenu(false);
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        int slotClicked = e.getSlot();
        ItemStack clickedItem = e.getCurrentItem();

        Inventory clickedInventory = e.getClickedInventory();
        Inventory topInventory = player.getOpenInventory().getTopInventory();

        if (clickedInventory == null) return;
        if (!topInventory.equals(inv) || !clickedInventory.equals(topInventory)) return;
        if (clickedItem == null && e.getCursor() == null) return;

        e.setCancelled(true);

        if(slotClicked == 11){

            BlackCloverPlugin blackCloverPlugin = (BlackCloverPlugin) plugin;
            PlayerData playerData = ((BlackCloverPlugin) plugin).getPlayerDataManager().getData(player);
            int manaIncrease = blackCloverPlugin.getBaseConfig().getRegenIncreasePerUpgrade();
            String successMessage;
            int numPoints = playerData.getNumUnusedPoints();

            if(numPoints == 0){
                player.closeInventory();
                player.sendMessage("You do not have enough points to do anything!");
                return;
            }

            if(spellUnlocking == null){
                int newMaxRegen = manaIncrease + playerData.getMaxRegen();
                playerData.decreasePoints(1);
                playerData.setMaxRegen(newMaxRegen);
                successMessage = "You have upgraded your max mana to &b&l" + newMaxRegen;
            }else{

                SpellType spellType = spellUnlocking.getKEY();
                boolean isUltimate = spellType.isUltimate();
                if(isUltimate && playerData.getNumUnusedPoints() < 5){
                    player.closeInventory();
                    player.sendMessage("You don't have enough points to unlock this spell!");
                    return;
                }

                if(isUltimate){
                    playerData.decreasePoints(5);
                }else{
                    playerData.decreasePoints(1);
                }

                playerData.getUnlockedSpells().add(spellType);
                successMessage = "You have unlocked the spell &b&l" + spellType.toProperName();

            }
            player.closeInventory();
            player.sendMessage(StringUtils.colorString(successMessage));

        }else if(slotClicked == 15){
            previousMenu.initializeItems(player);
            player.closeInventory();
            previousMenu.openInventory(player);
        }

    }

}
