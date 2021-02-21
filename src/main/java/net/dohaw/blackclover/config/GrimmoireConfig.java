package net.dohaw.blackclover.config;

import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.corelib.Config;

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

}
