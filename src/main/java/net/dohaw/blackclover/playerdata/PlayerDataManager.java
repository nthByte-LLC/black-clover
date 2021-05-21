package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.config.BaseConfig;
import net.dohaw.blackclover.config.PlayerDataConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.PDCHandler;
import net.dohaw.corelib.ProbabilityUtilities;
import net.dohaw.corelib.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerDataManager {

    @Getter
    private Map<UUID, PlayerData> playerData = new HashMap<>();

    private BlackCloverPlugin plugin;

    public PlayerDataManager(BlackCloverPlugin plugin){
        this.plugin = plugin;
        /*
            Removes entries that have no tasks or have all their tasks canceled. Usually, tasks deal with this on their own but just in case there's some lingering tasks, we deal with it here.
         */
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for(PlayerData data : playerData.values()){
                Map<SpellType, List<BukkitTask>> spellTasks = data.getSpellRunnables();
                for(Map.Entry<SpellType, List<BukkitTask>> entry : spellTasks.entrySet()){
                    List<BukkitTask> tasks = entry.getValue();
                    tasks.removeIf(BukkitTask::isCancelled);
                }
                spellTasks.entrySet().removeIf(e -> e.getValue().isEmpty());
            }
        }, 0, 1200L);

    }

    public PlayerData getData(UUID uuid){
        return playerData.get(uuid);
    }

    public PlayerData getData(Player player){
        return playerData.get(player.getUniqueId());
    }

    /**
     * Loads player data. Usually used when a player joins the server.
     * @param player The player you want to load data for.
     */
    public void loadData(Player player){

        UUID uuid = player.getUniqueId();
        File file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "player_data", uuid.toString() + ".yml");
        if(file.exists()){
            PlayerDataConfig pdc = new PlayerDataConfig(file);
            PlayerData pd = pdc.loadData(uuid);
            initManaBar(pd);
            playerData.put(pd.getUUID(), pd);
        }else{
            createData(player);
        }

    }

    /**
     * Pretty much only useful if you reload the plugin via plugman and it needs to reload the player data for the players that are on
     */
    public void loadAllData(){
        for(Player player : Bukkit.getOnlinePlayers()){
            loadData(player);
        }
    }

    /**
     * Saves all the player data. Usually used on plugin prepareShutdown.
     * Also removes player's mana bars.
     */
    public void shutdown(){
        for(PlayerData pd : playerData.values()){
            UUID uuid = pd.getUUID();
            Map<UUID, BossBar> manaBars = plugin.getRegenBars();
            if(manaBars.containsKey(uuid)){
                BossBar manaBar = manaBars.get(uuid);
                manaBar.removeAll();
            }
            pd.prepareDataRemoval();
        }
    }

    /**
     * Method to create Player data. Usually used if a player has never joined the server before.
     * @param player
     */
    private PlayerData createData(Player player){

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
            randomGrimmoire.adaptItemStack(pd, grimmoire);

            if(baseConfig.isInTestingMode()){
                System.out.println("Random Tier: " + randomTier);
                System.out.println("Grimmoire Acquired: " + randomGrimmoire.getKEY());
            }else{
                PDCHandler.markGrimmoire(grimmoire, randomGrimmoire.getKEY());
                if(randomTier == 5 || randomTier == 4){
                    baseConfig.setWhoHasIt(player, randomTier);
                }
            }

            int maxMana = plugin.getMaxRegen(randomGrimmoire.getTier());
            pd.setGrimmoireWrapper(randomGrimmoire);
            pd.setMaxRegen(maxMana);
            pd.setRegenAmount(0);

            initManaBar(pd);

            playerData.put(player.getUniqueId(), pd);
            player.getInventory().setItemInOffHand(grimmoire);

            return pd;

        }else{
            plugin.getLogger().warning("There has been an error creating data for the player " + Bukkit.getPlayer(uuid).getName());
        }

        return null;

    }

    /**
     * Intializes the mana bar that is at the top of a player's screen.
     */
    public void initManaBar(PlayerData pd){

        Player player = pd.getPlayer();
        GrimmoireWrapper grimmoireWrapper = pd.getGrimmoireWrapper();
        int maxMana = pd.getMaxRegen();
        BarColor barColor = Grimmoire.colorCodeToBarColor(grimmoireWrapper.getConfig().getDisplayNameColor());
        BossBar bar = Bukkit.createBossBar(StringUtils.colorString("&bMana: &f" + maxMana + "/" + maxMana), barColor, BarStyle.SOLID);
        bar.addPlayer(player);
        plugin.getRegenBars().put(player.getUniqueId(), bar);
    }

    /**
     * Gets a random tier. Certain tiers may not be available to achieve if they are limited and another player has a tier grimmoire already. (For example, 4 and 5 are limited)
     * @return A random grimmoire tier
     */
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

    /**
     * Gets a random grimmoire according to the tier. Usually used for when players first join the server and don't have a grimmoire
     * @param tier The tier number
     * @return A random grimmoire
     */
    private GrimmoireWrapper getRandomGrimmoire(int tier){
        List<GrimmoireWrapper> tierWrappers = Grimmoire.getByTier(tier);
        Random rand = new Random();
        return tierWrappers.get(rand.nextInt(tierWrappers.size()));
    }

    /**
     * Simply removes the data from the map that contains the player data
     * @param uuid The player's UUID
     */
    public void removeDataFromMemory(UUID uuid){
        playerData.remove(uuid);
    }

    /**
     * Uses the config tied to the PlayerData object and saves the PlayerData properties in its yml file.
     * @param uuid The player's UUID
     */
    public void saveData(UUID uuid){
        PlayerData pd = getData(uuid);
        pd.prepareDataRemoval();
    }

}
