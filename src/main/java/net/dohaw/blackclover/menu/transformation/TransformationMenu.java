package net.dohaw.blackclover.menu.transformation;

import net.dohaw.blackclover.event.PostCastSpellEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class TransformationMenu extends Menu implements Listener {

    public TransformationMenu(JavaPlugin plugin, Menu previousMenu, String menuTitle, int numSlots) {
        super(plugin, previousMenu, menuTitle, numSlots);
        JPUtils.registerEvents(this);
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

        if(clickedItem != null){
            if(clickedItem.getType() != fillerMat){

                morph(e);
                player.closeInventory();
                PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player);

                CastSpellWrapper transformationSpell = getTransformationSpell();
                transformationSpell.deductMana(pd);
                // Starts cooldown
                Bukkit.getPluginManager().callEvent(new PostCastSpellEvent(pd, transformationSpell, true));

            }
        }

    }

    public abstract void morph(InventoryClickEvent e);

    public abstract CastSpellWrapper getTransformationSpell();

}
