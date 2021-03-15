package net.dohaw.blackclover.config;

import net.dohaw.blackclover.grimmoire.type.Water;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.playerdata.ShakudoPlayerData;
import net.dohaw.blackclover.playerdata.SpecifiableData;
import net.dohaw.blackclover.playerdata.WaterPlayerData;
import net.dohaw.corelib.Config;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerDataConfig extends Config {

    private final List<GrimmoireType> hasSpecialPlayerData = Arrays.asList(GrimmoireType.SHAKUDO);

    public PlayerDataConfig(File file){
        super(file);
    }

    public PlayerData loadData(UUID uuid){

        String grimmoireTypeStr = config.getString("Grimmoire Type");
        GrimmoireType grimmoireType = GrimmoireType.valueOf(grimmoireTypeStr.toUpperCase());
        GrimmoireWrapper grimmoireWrapper = (GrimmoireWrapper) Grimmoire.getByKey(grimmoireType);
        PlayerData pd = new PlayerData(uuid, grimmoireWrapper);

        pd.setMaxRegen(config.getInt("Max Regen"));
        pd.setRegenAmount(config.getInt("Regen Amount"));
        pd.setConfig(this);

        PlayerData newData;
        if(hasSpecialPlayerData.contains(grimmoireType)){
            if(grimmoireType == GrimmoireType.SHAKUDO) {
                newData = new ShakudoPlayerData(pd.getUuid());
            }else if(grimmoireType == GrimmoireType.WATER){
                newData = new WaterPlayerData(pd.getUuid());
            }else{
                // If it gets to here, then there's a conflict between what's in the list and what is being checked in the if statement chain.
                return pd;
            }
            // merges the values over such as max regen, regen amount, etc
            newData.merge(pd);
            // Can do this because it's always going to be a class that implements this unless there is human error.
            ((SpecifiableData) newData).loadSpecifiedData(config);
            return newData;
        }

        return pd;

    }

    public void saveData(PlayerData pd){
        config.set("Max Regen", pd.getMaxRegen());
        config.set("Regen Amount", pd.getRegenAmount());
        config.set("Grimmoire Type", pd.getGrimmoireWrapper().getKEY().toString());
        if(pd instanceof SpecifiableData){
            SpecifiableData spd = (SpecifiableData) pd;
            spd.saveSpecifiedData(config);
            System.out.println("SAVING SPECIFIED DATA");
        }
        saveConfig();
    }

}
