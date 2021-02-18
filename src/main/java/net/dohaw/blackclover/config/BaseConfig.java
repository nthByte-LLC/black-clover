package net.dohaw.blackclover.config;

import net.dohaw.corelib.Config;
import net.dohaw.corelib.helpers.ItemStackHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BaseConfig extends Config {

    public BaseConfig() {
        super("config.yml");
    }

    public int getObtainingChance(int tier){
        return config.getInt("Obtaining Chance.Tier " + tier);
    }

    public int getQuantity(int tier){
        return config.getInt("Quantity of Grimmoire.Tier " + tier);
    }

    public Material getGrimmoireMaterial(){
        return Material.valueOf(config.getString("Grimmoire Properties.Material", "WRITABLE_BOOK"));
    }

    public boolean isGrimmoireTierAvailable(int tier){
        if(tier == 2 || tier == 3){
            return true;
        }else if(tier == 4 || tier == 5){
            System.out.println("TEST: " + config.getString("Acquisition Info.Tier " + tier + ".Who Has It"));
            return config.getString("Acquisition Info.Tier " + tier + ".Who Has It") != null;
        }else{
            throw new IllegalArgumentException("Unexpected Tier!");
        }
    }

    public void setWhoHasIt(Player whoHasIt, int tier){
        if(tier != 4 && tier != 5) throw new IllegalArgumentException("The only tiers that you can use this method for are tiers 4 and 5!");
        config.set("Acquisition Info.Tier " + tier + ".Who Has It", whoHasIt.getName());
    }

    public ItemStack createBaseGrimmoire(){
        Material mat = getGrimmoireMaterial();
        ItemStack grimmoire = new ItemStack(mat, 1);
        String displayName = config.getString("Grimmoire Properties.Display Name");
        List<String> lore = config.getStringList("Grimmoire Properties.Lore");
        ItemMeta meta = grimmoire.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        grimmoire.setItemMeta(meta);
        ItemStackHelper.addGlowToItem(grimmoire);
        return grimmoire;
    }

    public int getTierBaseMana(int tier){
        return config.getInt("Base Mana.Tier " + tier);
    }

    public boolean isInTestingMode(){
        return config.getBoolean("Testing Mode");
    }

}
