package net.dohaw.blackclover.util;

import lombok.NonNull;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class NBTHandler {

    public static final NamespacedKey GRIMMOIRE_TYPE_PDC_KEY = NamespacedKey.minecraft("grimmoire_type");

    public static boolean hasGrimmoire(@NonNull Player player){

        PlayerInventory inv = player.getInventory();
        for(ItemStack stack : inv.getContents()){
            if(stack != null){
                ItemMeta meta = stack.getItemMeta();
                if(meta != null){
                    PersistentDataContainer pdc = meta.getPersistentDataContainer();
                    return pdc.has(GRIMMOIRE_TYPE_PDC_KEY, PersistentDataType.STRING);
                }
            }
        }

        return false;

    }

    public static void markGrimmoire(@NonNull ItemStack stack, GrimmoireType grimmoireType){
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(GRIMMOIRE_TYPE_PDC_KEY, PersistentDataType.STRING, grimmoireType.toString().toLowerCase());
        stack.setItemMeta(meta);
    }

}
