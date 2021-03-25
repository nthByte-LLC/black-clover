package net.dohaw.blackclover.config;

import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.corelib.Config;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.List;

public class GrimmoireConfig extends Config {

    public GrimmoireConfig(String fileName) {
        super(fileName);
    }

    public String getDisplayNameColor(){
        return config.getString("Display Name Color");
    }

    public int getIntegerSetting(SpellType spellType, String settingKey){
        return config.getInt("Spell Settings." + spellType.getConfigKey() + "." + settingKey);
    }

    public double getDoubleSetting(SpellType spellType, String settingKey){
        return config.getDouble("Spell Settings." + spellType.getConfigKey() + "." + settingKey);
    }

    public String getStringSetting(SpellType spellType, String settingKey){
        return config.getString("Spell Settings." + spellType.getConfigKey() + "." + settingKey);
    }

    public Particle getParticle(SpellType spellType){
        return Particle.valueOf(config.getString("Spell Settings." + spellType.getConfigKey() + ".Particle", "FLAME"));
    }

}
