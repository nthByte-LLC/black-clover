package net.dohaw.blackclover.menu;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.PlayerDataManager;
import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class GrimmoireMenu extends Menu implements Listener {

    private Map<Integer, SpellWrapper> slotsOfSpells = new HashMap<>();

    public GrimmoireMenu(JavaPlugin plugin) {
        super(plugin, null, "Grimmoire Menu", 54);
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player p) {

        PlayerDataManager playerDataManager = ((BlackCloverPlugin)plugin).getPlayerDataManager();
        PlayerData playerData = playerDataManager.getData(p);

        int[] spellSlots = {28, 30, 32, 34, 39, 41};
        List<SpellWrapper> spells = new ArrayList<>(playerData.getGrimmoireWrapper().getSpells().values());

        inv.setItem(13, createGuiItem(Material.BLUE_WOOL, "&b&lUpgrade Mana", Arrays.asList("&f&lCurrent Max Mana: &b&l" + playerData.getMaxRegen())));
        for (int i = 0; i < spellSlots.length; i++) {

            SpellWrapper spell = spells.get(i);
            // If all 6 spells aren't registered for some reason, it'll return null eventually.
            if(spell != null){

                Material material;
                List<String> lore = new ArrayList<>();

                System.out.println("UNLOCKED SPLELS: " + playerData.getUnlockedSpells());
                if(playerData.isSpellUnlocked(spell)){
                    material = Material.DIAMOND_SWORD;
                    lore.add("&a&lUnlocked");
                }else{
                    material = Material.WOODEN_SWORD;
                    lore.add("&f&lUnlock this spell");
                }
                String spellName = spells.get(i).getKEY().toProperName();
                int slotSpell = spellSlots[i];
                inv.setItem(slotSpell, createGuiItem(material, "&b&l" + spellName, lore));
                slotsOfSpells.put(slotSpell, spell);

            }

        }

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

        ConfirmationMenu nextMenu;
        if(clickedItem != null && clickedItem.getType() != fillerMat){
            if(slotClicked == 13){
                nextMenu = new ConfirmationMenu(plugin, this, null);
            }else{
                nextMenu = new ConfirmationMenu(plugin, this, slotsOfSpells.get(slotClicked));
            }
            nextMenu.initializeItems(player);
            nextMenu.openInventory(player);
        }

    }

}
