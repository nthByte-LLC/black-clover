package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.config.PlayerDataConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    @Getter
    private Map<UUID, PlayerData> playerData = new HashMap<>();

    private BlackCloverPlugin plugin;

    public PlayerDataManager(BlackCloverPlugin plugin){
        this.plugin = plugin;
    }

    public Map<UUID, PlayerData> getAllData(){
        return playerData;
    }

    public PlayerData getData(UUID uuid){
        return playerData.get(uuid);
    }

    public void loadData(UUID uuid){
        File file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "player_data", uuid.toString() + ".yml");
        if(file.exists()){
            PlayerDataConfig pdc = new PlayerDataConfig(file);
            pdc.loadData(uuid);
        }else{
            createData(uuid);
        }
    }

    private void createData(UUID uuid){

        File file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "player_data", uuid.toString() + ".yml");
        boolean hasFileBeenCreated = false;
        try {
            hasFileBeenCreated = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(hasFileBeenCreated){
            PlayerData pd = new PlayerData(uuid);
        }else{
            plugin.getLogger().warning("There has been an error creating data for the player " + Bukkit.getPlayer(uuid).getName());
        }

    }

    public void saveData(UUID uuid){
        PlayerData pd = playerData.get(uuid);
        pd.getConfig().saveData(pd);
    }

}
