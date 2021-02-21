package net.dohaw.blackclover.util;

import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.corelib.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemStackUtil {

    public static ItemStack createStack(SpellWrapper spellWrapper, Material material, String displayName, List<String> lore){
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(spellWrapper.nsk(), PersistentDataType.STRING, "marker");
        meta.setDisplayName(StringUtils.colorString(displayName));
        meta.setLore(StringUtils.colorLore(lore));
        stack.setItemMeta(meta);
        return stack;
    }

}
