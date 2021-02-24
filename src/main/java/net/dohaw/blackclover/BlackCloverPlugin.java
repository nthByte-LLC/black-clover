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
import net.dohaw.blackclover.runnable.ManaRegener;
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
        new ManaRegener(this).runTaskTimer(this, 0L, 20L);
        JPUtils.registerCommand("blackclover", new BlackCloverCommand(this));

    }

    @Override
    public void onDisable() {
        playerDataManager.saveAllData();
    }

    private void registerGrimmoires(){
        Grimmoire.setInstance(this);
        Grimmoire.registerWrapper(Grimmoire.SAND);
        Grimmoire.registerWrapper(Grimmoire.FIRE);
        Grimmoire.registerWrapper(Grimmoire.ANTI);
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
        this.t2MaxMana = baseConfig.getTierMaxMana(2);
        this.t3MaxMana = baseConfig.getTierMaxMana(3);
        this.t4MaxMana = baseConfig.getTierMaxMana(4);
        this.t5MaxMana = baseConfig.getTierMaxMana(5);
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


    public void updateManaBar(PlayerData pd){

        BossBar manaBar = manaBars.get(pd.getUuid());
        double manaAmount = pd.getManaAmount();
        double maxMana = pd.getMaxMana();
        double percentageManaFull = manaAmount / maxMana;

        manaBar.setProgress(percentageManaFull);
        manaBar.setTitle(StringUtils.colorString("&bMana: &f" + (int)manaAmount + " / " + (int)maxMana));

    }

    public void removeManaBar(Player player){
        this.removeManaBar(player.getUniqueId());
    }

    public void removeManaBar(UUID uuidPlayer){
        BossBar bossBar = manaBars.get(uuidPlayer);
        bossBar.removeAll();
        manaBars.remove(uuidPlayer);
    }

}
