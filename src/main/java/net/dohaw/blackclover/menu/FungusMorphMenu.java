package net.dohaw.blackclover.menu;

import net.dohaw.blackclover.event.PostCastSpellEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.type.compass.Homestone;
import net.dohaw.blackclover.grimmoire.spell.type.fungus.Morph;
import net.dohaw.blackclover.playerdata.FungusPlayerData;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class FungusMorphMenu extends Menu implements Listener {

    public FungusMorphMenu(JavaPlugin plugin) {
        super(plugin, null, "Morph Menu", 9);
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player p) {

        List<String> emptyList = new ArrayList<>();
        inv.addItem(createGuiItem(Material.RED_MUSHROOM_BLOCK, "Red Mushroom", emptyList));
        inv.addItem(createGuiItem(Material.BROWN_MUSHROOM_BLOCK, "Brown Mushroom", emptyList));
        inv.addItem(createGuiItem(Material.OAK_SAPLING, "Oak Tree", emptyList));
        inv.addItem(createGuiItem(Material.SPRUCE_SAPLING, "Spruce Tree", emptyList));
        inv.addItem(createGuiItem(Material.BIRCH_SAPLING, "Birch Tree", emptyList));
        inv.addItem(createGuiItem(Material.JUNGLE_SAPLING, "Jungle Tree", emptyList));
        inv.addItem(createGuiItem(Material.CACTUS, "Cactus", emptyList));
        inv.addItem(createGuiItem(Material.BARRIER, "Close Menu", emptyList));

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

        TreeType treeType = getTreeTypeClicked(slotClicked);
        if(treeType == null){
            player.closeInventory();
        }else{
            morphPlayer(player, treeType);
        }

    }

    private TreeType getTreeTypeClicked(int slotClicked){

        switch(slotClicked){
            case 0:
                return TreeType.RED_MUSHROOM;
            case 1:
                return TreeType.BROWN_MUSHROOM;
            case 2:
                return TreeType.TREE;
            case 3:
                return TreeType.REDWOOD;
            case 4:
                return TreeType.BIRCH;
            case 5:
                return TreeType.JUNGLE;
            case 6:
                return TreeType.CHORUS_PLANT;
            default:
                // It'll be null if there was a human error in aligning the slot clicked with the tree type.
                return null;
        }

    }

    private void morphPlayer(Player player, TreeType treeType){

        if(treeType != null){

            FungusPlayerData data = (FungusPlayerData) Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
            boolean hasPhysicallyMorphed = true;
            Location morphLocation = player.getLocation().clone();
            data.setMorphLocation(morphLocation);

            if(treeType != TreeType.CHORUS_PLANT){
                World world = player.getWorld();
                hasPhysicallyMorphed = world.generateTree(morphLocation, treeType);
            }else{

                List<Location> cactusBlockLocations = new ArrayList<>();
                // We make a cactus
                for(int i = 0; i < 3; i++){
                    Location cactusBlockLocation = morphLocation.clone().add(0, i, 0);
                    Block currentBlock = cactusBlockLocation.getBlock();
                    currentBlock.setType(Material.CACTUS, false);
                    cactusBlockLocations.add(cactusBlockLocation);
                }
                data.setCactusBlockLocations(cactusBlockLocations);

            }

            // Teleports player behind the morphed structure
            Location playerTPLocation = LocationUtil.getAbsoluteLocationInBack(morphLocation.clone(), 2);
            player.teleport(playerTPLocation);

            if(hasPhysicallyMorphed){

                player.closeInventory();

                Inventory playerInventory = player.getInventory();
                ItemStack[] contents = playerInventory.getContents();
                data.setItemsBeforeMorphing(contents);
                playerInventory.clear();

                player.setInvisible(true);
                data.setMorphType(treeType);
                data.setMorphed(true);
                data.setFrozen(true);
                data.setCanCast(false);

                Morph spell = Grimmoire.FUNGUS.morph;
                spell.deductMana(data);
                // Starts cooldown when you actually start teleporting
                Bukkit.getPluginManager().callEvent(new PostCastSpellEvent(data, spell, true));

            }else{
                player.sendMessage("There isn't sufficient space to morph here!");
                player.closeInventory();
            }

        }

    }

}
