package net.dohaw.blackclover;

import lombok.Getter;
import net.dohaw.blackclover.config.BaseConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.listener.PlayerWatcher;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.PlayerDataManager;
import net.dohaw.blackclover.runnable.Regenerator;
import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.StringUtils;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BlackCloverPlugin extends JavaPlugin {

    @Getter
    private String prefix;

    @Getter
    private PlayerDataManager playerDataManager;

    @Getter
    private Map<UUID, BossBar> regenBars = new HashMap<>();

    @Getter
    private int t2MaxRegen, t3MaxRegen, t4MaxRegen, t5MaxRegen;

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
                put("player_data", new File(getDataFolder() + File.separator + "data"));
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
        new Regenerator(this).runTaskTimer(this, 0L, 20L);
        JPUtils.registerCommand("blackclover", new BlackCloverCommand(this));

        playerDataManager.loadAllData();

    }

    @Override
    public void onDisable() {
        playerDataManager.shutdown();
    }

    private void registerGrimmoires(){
        Grimmoire.setInstance(this);
        Grimmoire.registerWrapper(Grimmoire.SAND);
        Grimmoire.registerWrapper(Grimmoire.FIRE);
        Grimmoire.registerWrapper(Grimmoire.ANTI);
        Grimmoire.registerWrapper(Grimmoire.SHAKUDO);
        Grimmoire.registerWrapper(Grimmoire.WATER);
        for(Wrapper wrapper : Grimmoire.wrappers.values()){
            if(wrapper instanceof GrimmoireWrapper){
                GrimmoireWrapper grimmoireWrapper = (GrimmoireWrapper) wrapper;
                for(SpellWrapper spell : grimmoireWrapper.getSpells().values()){
                    if(spell instanceof Listener){
                        JPUtils.registerEvents((Listener) spell);
                    }
                }
            }
        }
    }

    private void loadConfigValues(){
        this.t2MaxRegen = baseConfig.getTierMaxRegen(2);
        this.t3MaxRegen = baseConfig.getTierMaxRegen(3);
        this.t4MaxRegen = baseConfig.getTierMaxRegen(4);
        this.t5MaxRegen = baseConfig.getTierMaxRegen(5);
        this.prefix = baseConfig.getPrefix();
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

    public int getMaxRegen(int tier){
        if(tier == 2){
            return t2MaxRegen;
        }else if(tier == 3){
            return t3MaxRegen;
        }else if(tier == 4){
            return t4MaxRegen;
        }else{
            return t5MaxRegen;
        }
    }

    public static ItemStack getBaseGrimmoire(){
        return baseGrimmoire.clone();
    }


    public void updateRegenBar(PlayerData pd){

        BossBar regenBar = regenBars.get(pd.getUuid());
        double regenAmount = pd.getRegenAmount();
        double maxRegen = pd.getMaxRegen();
        double percentageRegenFull = regenAmount / maxRegen;

        regenBar.setProgress(percentageRegenFull);
        regenBar.setTitle(StringUtils.colorString("&bRegen: &f" + (int)regenAmount + " / " + (int)maxRegen));

    }

    public void removeRegenBar(Player player){
        this.removeRegenBar(player.getUniqueId());
    }

    public void removeRegenBar(UUID uuidPlayer){
        BossBar bossBar = regenBars.get(uuidPlayer);
        bossBar.removeAll();
        regenBars.remove(uuidPlayer);
    }

}
