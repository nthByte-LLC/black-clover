package net.dohaw.blackclover.menu;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.event.PostCastSpellEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.compass.Homestone;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.StringUtils;
import net.dohaw.corelib.menus.Menu;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WaypointsMenu extends Menu implements Listener {

    private Map<String, Location> waypoints;

    public WaypointsMenu(Map<String, Location> waypoints, int numSlots) {
        super(Grimmoire.instance, null, "&b&lWaypoints", numSlots);
        this.waypoints = waypoints;
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player p) {
        for(Map.Entry<String, Location> entry : waypoints.entrySet()){
            String displayName = "&c&l" + entry.getKey();
            int distance = (int) p.getLocation().distance(entry.getValue());
            List<String> lore = Arrays.asList("&f&lDistance: " + distance);
            inv.addItem(createGuiItem(Material.BEACON, displayName, lore));
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

        if(clickedItem != null){
            if(clickedItem.getType() != fillerMat){

                ItemMeta clickedItemMeta = clickedItem.getItemMeta();
                if(clickedItemMeta.hasDisplayName()){
                    String clickedItemDisplayName = StringUtils.removeChatColor(clickedItem.getItemMeta().getDisplayName());
                    System.out.println("Clicked item display name: " + clickedItemDisplayName);
                    if(waypoints.containsKey(clickedItemDisplayName)){
                        startTeleporting(player, waypoints.get(clickedItemDisplayName));
                    }
                }

            }
        }

    }

    private void startTeleporting(Player player, Location location){

        PlayerData pd = ((BlackCloverPlugin)plugin).getPlayerDataManager().getData(player.getUniqueId());
        Particle.DustOptions do1 = new Particle.DustOptions(Color.WHITE, 1.5f);
        Particle.DustOptions do2 = new Particle.DustOptions(Color.BLACK, 1.5f);
        BukkitTask[] tasks = SpellUtils.startDoubleTornadoParticles(player, do1, do2, true, 1);
        pd.addSpellRunnables(SpellType.HOMESTONE, tasks);
        player.closeInventory();

        Homestone spell = Grimmoire.COMPASS.homestone;
        spell.deductMana(pd);
        // Starts cooldown when you actually start teleporting
        Bukkit.getPluginManager().callEvent(new PostCastSpellEvent(pd, spell, true));

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            pd.stopSpellRunnables(SpellType.HOMESTONE);
            player.teleport(location);
            SpellUtils.playSound(player, Sound.BLOCK_PORTAL_TRAVEL);
            // When they spawn at the waypoint. Dirty way to do it.
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                SpellUtils.spawnParticle(player, Particle.END_ROD, 20, 0.5f, 0.5f, 0.5f);
            },2 );
        }, 30L);

    }

}
