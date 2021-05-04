package net.dohaw.blackclover.menu.transformation;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.playerdata.TransformationPlayerData;
import net.dohaw.corelib.menus.Menu;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TransformationMobMenu extends TransformationMenu {

    public final Map<EntityType, Material> MOB_EGGS = new HashMap<>();
    protected Map<Integer, EntityType> entitiesInSlots = new HashMap<>();

    public TransformationMobMenu(JavaPlugin plugin, Menu previousMenu, String menuTitle, int numSlots) {
        super(plugin, previousMenu, menuTitle, numSlots);
        compileMobEggs();
    }

    @Override
    public void initializeItems(Player p) {

        List<String> lore = Collections.singletonList(net.dohaw.corelib.StringUtils.colorString("&fMorph into this mob"));
        int index = 0;
        for(Map.Entry<EntityType, Material> entry : MOB_EGGS.entrySet()){
            EntityType type = entry.getKey();
            Material mat = entry.getValue();
            String displayName = StringUtils.capitalize(type.name().toLowerCase().replace("_", " "));
            displayName = net.dohaw.corelib.StringUtils.colorString("&f&l" + displayName);
            inv.addItem(createGuiItem(mat, displayName, lore));
            entitiesInSlots.put(index, type);
            index++;
        }

        this.fillerMat = Material.BLACK_STAINED_GLASS_PANE;
        fillMenu(false);

    }

    @Override
    public void morph(InventoryClickEvent e) {

        Player morpher = (Player) e.getWhoClicked();
        int slotClicked = e.getSlot();
        EntityType entityClicked = entitiesInSlots.get(slotClicked);

        LivingEntity entity = (LivingEntity) morpher.getWorld().spawnEntity(morpher.getLocation(), entityClicked);
        entity.setAI(false);
        morpher.setInvisible(true);
        TransformationPlayerData tpd = (TransformationPlayerData) Grimmoire.instance.getPlayerDataManager().getData(morpher);
        tpd.setMorphedEntity(entity);

    }

    public abstract void compileMobEggs();

}
