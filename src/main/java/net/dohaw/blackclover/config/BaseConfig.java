package net.dohaw.blackclover.config;

import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.corelib.Config;
import org.bukkit.Material;

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

}
