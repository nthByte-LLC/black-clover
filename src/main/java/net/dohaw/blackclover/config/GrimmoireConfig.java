package net.dohaw.blackclover.config;

import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.corelib.Config;
import org.bukkit.Material;

import java.util.List;

public class GrimmoireConfig extends Config {

    public GrimmoireConfig(String fileName) {
        super(fileName);
    }

    public String getDisplayNameColor(){
        return config.getString("Display Name Color");
    }

    public int getNumberSetting(SpellType spellType, String settingKey){
        return config.getInt("Spell Settings." + spellType.getConfigKey() + "." + settingKey);
    }

    public int getManaUsed(SpellType spellType){
        return config.getInt("Spell Settings." + spellType.getConfigKey() + ".Mana Used");
    }

    public String getCustomItemDisplayName(SpellType spellType){
        return config.getString("Spell Settings." + spellType.getConfigKey() + ".Custom Item Properties.Display Name");
    }

    public List<String> getCustomItemLore(SpellType spellType){
        return config.getStringList("Spell Settings." + spellType.getConfigKey() + ".Custom Item Properties.Lore");
    }

    public int getCustomItemHotbarNum(SpellType spellType){
        return config.getInt("Spell Settings." + spellType.getConfigKey() + ".Custom Item Properties.Hotbar Number");
    }

    public Material getCustomItemMaterial(SpellType spellType){
        return Material.valueOf(config.getString("Spell Settings." + spellType.getConfigKey() + ".Custom Item Properties.Material", "APPLE"));
    }

}
