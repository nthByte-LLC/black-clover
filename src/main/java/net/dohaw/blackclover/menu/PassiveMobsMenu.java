package net.dohaw.blackclover.menu;

import net.dohaw.corelib.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PassiveMobsMenu extends Menu {

    public PassiveMobsMenu(JavaPlugin plugin) {
        super(plugin, null, "Transformation", 9);
    }

    @Override
    public void initializeItems(Player p) {
    }

    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

    }
}
