package net.dohaw.blackclover.util;

import lombok.NonNull;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PDCHandler {

    public static final NamespacedKey MANA_PDC = NamespacedKey.minecraft("bc_mana_amount");
    public static final NamespacedKey GRIMMOIRE_TYPE_PDC_KEY = NamespacedKey.minecraft("grimmoire_type");

    public static boolean hasGrimmoire(@NonNull Player player){

        PlayerInventory inv = player.getInventory();
        for(ItemStack stack : inv.getContents()){
            if(stack != null){
                if(isGrimmoire(stack)){
                   return true;
                }
            }
        }

        return false;

    }

    public static void markPlayer(@NonNull Player player, int maxMana){
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(MANA_PDC, PersistentDataType.INTEGER, 0);
    }

    public static int getMana(@NonNull Player player){
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        return pdc.get(MANA_PDC, PersistentDataType.INTEGER);
    }

    public static void markGrimmoire(@NonNull ItemStack stack, GrimmoireType grimmoireType){
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(GRIMMOIRE_TYPE_PDC_KEY, PersistentDataType.STRING, grimmoireType.toString().toLowerCase());
        stack.setItemMeta(meta);
    }

    public static boolean isGrimmoire(@NonNull ItemStack stack){
        ItemMeta meta = stack.getItemMeta();
        if(meta != null){
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            return pdc.has(GRIMMOIRE_TYPE_PDC_KEY, PersistentDataType.STRING);
        }
        return false;
    }

    public static GrimmoireWrapper getGrimmoireWrapper(Player player){

        for(ItemStack stack : player.getInventory().getContents()){
            if(isGrimmoire(stack)){
                ItemMeta meta = stack.getItemMeta();
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                String grimmoireTypeStr = pdc.get(GRIMMOIRE_TYPE_PDC_KEY, PersistentDataType.STRING);
                GrimmoireType grimmoireType = GrimmoireType.valueOf(grimmoireTypeStr.toUpperCase());
                return (GrimmoireWrapper) Grimmoire.getByKey(grimmoireType);
            }
        }

        return null;

    }

}
