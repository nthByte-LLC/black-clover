package net.dohaw.blackclover.util;

import lombok.NonNull;
import net.dohaw.blackclover.Wrapper;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PDCHandler {

    public static final NamespacedKey GRIMMOIRE_TYPE_PDC_KEY = NamespacedKey.minecraft("grimmoire_type");

//    public static boolean hasGrimmoire(@NonNull Player player){
//
//        PlayerInventory inv = player.getInventory();
//        for(ItemStack stack : inv.getContents()){
//            if(stack != null){
//                if(isGrimmoire(stack)){
//                   return true;
//                }
//            }
//        }
//
//        return false;
//
//    }

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

    public static ItemStack getGrimmoire(Player player){
        for(ItemStack stack : player.getInventory().getContents()){
            if(stack != null){
                if(isGrimmoire(stack)){
                    return stack;
                }
            }
        }
        return null;
    }

    public static CastSpellWrapper getSpellBoundToItem(PlayerData pd, ItemStack stack){
        GrimmoireWrapper grimmoireWrapper = pd.getGrimmoireWrapper();
        for(SpellWrapper spell : grimmoireWrapper.getSpells().values()){
            if(spell instanceof CastSpellWrapper){
                CastSpellWrapper cSpell = (CastSpellWrapper) spell;
                if(cSpell.isSpellBoundItem(stack)){
                    return cSpell;
                }
            }
        }
        return null;
    }

    /**
     * Gets the casted spell bound to the projectile
     * @param pd The caster
     * @param projectile The projectile that is marked
     * @return The spell that is bound to the projectile
     */
    public static CastSpellWrapper getSpellBoundToProjectile(PlayerData pd, Projectile projectile){
        GrimmoireWrapper grimmoireWrapper = pd.getGrimmoireWrapper();
        for(SpellWrapper spell : grimmoireWrapper.getSpells().values()){
            if(spell instanceof CastSpellWrapper){
                CastSpellWrapper cSpell = (CastSpellWrapper) spell;
                if(cSpell.isSpellBound(projectile)){
                    return cSpell;
                }
            }
        }
        return null;
    }

    /**
     * Gets the casted spell bound to the projectile
     * @param projectile
     * @return
     */
    public static CastSpellWrapper getSpellBoundToProjectile(Projectile projectile){
        for(Wrapper wrapper : Grimmoire.wrappers.values()){
            GrimmoireWrapper grimmoireWrapper = (GrimmoireWrapper) wrapper;
            for(SpellWrapper spell : grimmoireWrapper.getSpells().values()){
                if(spell instanceof CastSpellWrapper){
                    CastSpellWrapper cSpell = (CastSpellWrapper) spell;
                    if(cSpell.isSpellBound(projectile)){
                        return cSpell;
                    }
                }
            }
        }
        return null;
    }

    public static GrimmoireWrapper getRelatedGrimmoire(Projectile projectile){
        for(Wrapper wrapper : Grimmoire.wrappers.values()){
            GrimmoireWrapper grimmoireWrapper = (GrimmoireWrapper) wrapper;
            for(SpellWrapper spell : grimmoireWrapper.getSpells().values()){
                if(spell instanceof CastSpellWrapper){
                    CastSpellWrapper cSpell = (CastSpellWrapper) spell;
                    if(cSpell.isSpellBound(projectile)){
                        return grimmoireWrapper;
                    }
                }
            }
        }
        return null;
    }

}
