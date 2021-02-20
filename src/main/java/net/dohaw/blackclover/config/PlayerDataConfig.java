package net.dohaw.blackclover.config;

import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.corelib.Config;

import java.io.File;
import java.util.UUID;

public class PlayerDataConfig extends Config {

    public PlayerDataConfig(File file){
        super(file);
    }

    public PlayerData loadData(UUID uuid){

        String grimmoireTypeStr = config.getString("Grimmoire Type");
        GrimmoireType grimmoireType = GrimmoireType.valueOf(grimmoireTypeStr.toUpperCase());
        GrimmoireWrapper grimmoireWrapper = (GrimmoireWrapper) Grimmoire.getByKey(grimmoireType);
        PlayerData pd = new PlayerData(uuid, grimmoireWrapper);

        pd.setMaxMana(config.getInt("Max Mana"));
        pd.setManaAmount(config.getInt("Mana Amount"));
        pd.setConfig(this);

        return pd;

    }

    public void saveData(PlayerData pd){
        config.set("Max Mana", pd.getMaxMana());
        config.set("Mana Amount", pd.getManaAmount());
        config.set("Grimmoire Type", pd.getGrimmoireWrapper().getKEY().toString());
        saveConfig();
    }

}
