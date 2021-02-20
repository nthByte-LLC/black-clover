package net.dohaw.blackclover;

import lombok.Getter;
import net.dohaw.blackclover.config.BaseConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.listener.PlayerWatcher;
import net.dohaw.blackclover.playerdata.PlayerDataManager;
import net.dohaw.blackclover.runnable.ManaRegener;
import net.dohaw.blackclover.util.PDCHandler;
import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.ProbabilityUtilities;
import net.dohaw.corelib.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public final class BlackCloverPlugin extends JavaPlugin {

    @Getter
    private PlayerDataManager playerDataManager;

    @Getter
    private Map<UUID, BossBar> manaBars = new HashMap<>();

    @Getter
    private int t2MaxMana, t3MaxMana, t4MaxMana, t5MaxMana;

    private static ItemStack baseGrimmoire;

    @Getter
    private BaseConfig baseConfig;

    @Override
    public void onEnable() {

        CoreLib.setInstance(this);
        JPUtils.validateFilesOrFolders(
            new HashMap<String, Object>(){{
                put("data", getDataFolder());
                put("grimmoires", getDataFolder());
                put("player_data", getDataFolder() + File.separator + "data");
            }}
            ,true
        );
        validateGrimmoireFiles();
        JPUtils.validateFiles("config.yml");

        this.baseConfig = new BaseConfig();
        loadConfigValues();
        baseGrimmoire = baseConfig.createBaseGrimmoire();

        this.playerDataManager = new PlayerDataManager(this);
        JPUtils.registerEvents(new PlayerWatcher(this));
        registerGrimmoires();
        new ManaRegener(this).runTaskTimer(this, 0L, 20L);
    }

    @Override
    public void onDisable() {

    }

    private void registerGrimmoires(){
        Grimmoire.registerWrapper(Grimmoire.SAND);
        Grimmoire.registerWrapper(Grimmoire.FIRE);
        Grimmoire.registerWrapper(Grimmoire.ANTI);
    }

    private void loadConfigValues(){
        this.t2MaxMana = baseConfig.getTierMaxMana(2);
        this.t3MaxMana = baseConfig.getTierMaxMana(3);
        this.t4MaxMana = baseConfig.getTierMaxMana(4);
        this.t5MaxMana = baseConfig.getTierMaxMana(5);
    }

    private void validateGrimmoireFiles(){
        HashMap<String, Object> fileInfo = new HashMap<>();
        File folder = new File(getDataFolder(), "grimmoires");
        for(GrimmoireType type : GrimmoireType.values()){
            String resourceFileName = type.toString().toLowerCase() + ".yml";
            fileInfo.put(resourceFileName, folder);
        }
        JPUtils.validateFilesOrFolders(fileInfo, false);
    }

    public int getMaxMana(int tier){
        if(tier == 2){
            return t2MaxMana;
        }else if(tier == 3){
            return t3MaxMana;
        }else if(tier == 4){
            return t4MaxMana;
        }else{
            return t5MaxMana;
        }
    }

    public static ItemStack getBaseGrimmoire(){
        return baseGrimmoire.clone();
    }

    private void giveRandomGrimmoire(Player player){

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

        int randomTier = (int) pu.getRandomElement();
        List<GrimmoireWrapper> tierWrappers = Grimmoire.getByTier(randomTier);

        Random rand = new Random();
        GrimmoireWrapper randomGrimmoire = tierWrappers.get(rand.nextInt(tierWrappers.size()));

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

        initManaUser(player, randomGrimmoire);
        player.getInventory().addItem(grimmoire);

    }

    private void initManaUser(Player player, GrimmoireWrapper grimmoireWrapper){
        int maxMana = this.getMaxMana(grimmoireWrapper.getTier());
        PDCHandler.markPlayer(player, this.getMaxMana(grimmoireWrapper.getTier()));
        BossBar bar = Bukkit.createBossBar(StringUtils.colorString("&bMana: &f" + maxMana + "/" + maxMana), BarColor.BLUE, BarStyle.SOLID);
        bar.addPlayer(player);
        manaBars.put(player.getUniqueId(), bar);
    }

}
