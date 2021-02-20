package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.config.BaseConfig;
import net.dohaw.blackclover.config.PlayerDataConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.util.PDCHandler;
import net.dohaw.corelib.ProbabilityUtilities;
import net.dohaw.corelib.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerDataManager {

    @Getter
    private Map<UUID, PlayerData> playerData = new HashMap<>();

    private BlackCloverPlugin plugin;

    public PlayerDataManager(BlackCloverPlugin plugin){
        this.plugin = plugin;
    }

    public PlayerData getData(UUID uuid){
        return playerData.get(uuid);
    }

    public void loadData(Player player){

        UUID uuid = player.getUniqueId();
        File file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "player_data", uuid.toString() + ".yml");
        if(file.exists()){
            PlayerDataConfig pdc = new PlayerDataConfig(file);
            PlayerData pd = pdc.loadData(uuid);
            initManaBar(player, pd.getGrimmoireWrapper());
            playerData.put(pd.getUuid(), pd);
        }else{
            createData(player);
        }

    }

    public void saveAllData(){
        for(PlayerData pd : playerData.values()){
            pd.saveData();
        }
    }

    private void createData(Player player){

        UUID uuid = player.getUniqueId();
        File file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "player_data", uuid.toString() + ".yml");
        boolean hasFileBeenCreated = false;
        try {
            hasFileBeenCreated = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(hasFileBeenCreated){

            PlayerDataConfig playerDataConfig = new PlayerDataConfig(file);
            PlayerData pd = new PlayerData(uuid);
            pd.setConfig(playerDataConfig);

            BaseConfig baseConfig = plugin.getBaseConfig();

            int randomTier = getRandomTier();
            GrimmoireWrapper randomGrimmoire = getRandomGrimmoire(randomTier);

            ItemStack grimmoire = BlackCloverPlugin.getBaseGrimmoire();
            randomGrimmoire.adaptItemStack(grimmoire);

            if(baseConfig.isInTestingMode()){
                System.out.println("Random Tier: " + randomTier);
                System.out.println("Grimmoire Acquired: " + randomGrimmoire.getKEY());
            }else{
                PDCHandler.markGrimmoire(grimmoire, (GrimmoireType) randomGrimmoire.getKEY());
                if(randomTier == 5 || randomTier == 4){
                    baseConfig.setWhoHasIt(player, randomTier);
                }
            }

            int maxMana = plugin.getMaxMana(randomGrimmoire.getTier());
            pd.setGrimmoireWrapper(randomGrimmoire);
            pd.setMaxMana(maxMana);
            pd.setManaAmount(maxMana);

            initManaBar(player, randomGrimmoire);

            playerData.put(player.getUniqueId(), pd);
            player.getInventory().addItem(grimmoire);

        }else{
            plugin.getLogger().warning("There has been an error creating data for the player " + Bukkit.getPlayer(uuid).getName());
        }

    }

    private void initManaBar(Player player, GrimmoireWrapper grimmoireWrapper){
        int maxMana = plugin.getMaxMana(grimmoireWrapper.getTier());
        BossBar bar = Bukkit.createBossBar(StringUtils.colorString("&bMana: &f" + maxMana + "/" + maxMana), BarColor.BLUE, BarStyle.SOLID);
        bar.addPlayer(player);
        plugin.getManaBars().put(player.getUniqueId(), bar);
    }

    private int getRandomTier(){

        BaseConfig baseConfig = plugin.getBaseConfig();

        List<Integer> tiersAvailable = new ArrayList<>();
        tiersAvailable.add(2);
        tiersAvailable.add(3);

        boolean isTierFourAvailable = baseConfig.isGrimmoireTierAvailable(4);
        if(isTierFourAvailable){
            tiersAvailable.add(4);
        }

        boolean isTierFiveAvailable = baseConfig.isGrimmoireTierAvailable(5);
        if(isTierFiveAvailable){
            tiersAvailable.add(5);
        }

        ProbabilityUtilities pu = new ProbabilityUtilities();
        for(int tier : tiersAvailable){
            int obtainingChance = baseConfig.getObtainingChance(tier);
            pu.addChance(tier, obtainingChance);
        }

        return (int) pu.getRandomElement();

    }

    private GrimmoireWrapper getRandomGrimmoire(int tier){
        List<GrimmoireWrapper> tierWrappers = Grimmoire.getByTier(tier);
        Random rand = new Random();
        return tierWrappers.get(rand.nextInt(tierWrappers.size()));
    }

    public void removeDataFromMemory(UUID uuid){
        playerData.remove(uuid);
    }

    public void saveData(UUID uuid){
        PlayerData pd = playerData.get(uuid);
        pd.getConfig().saveData(pd);
    }

}
