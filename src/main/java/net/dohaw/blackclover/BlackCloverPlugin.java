package net.dohaw.blackclover;

import lombok.Getter;
import net.dohaw.blackclover.config.BaseConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.listener.PlayerWatcher;
import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public final class BlackCloverPlugin extends JavaPlugin {



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
            }}
            ,true
        );
        validateGrimmoireFiles();
        JPUtils.validateFiles("config.yml");

        this.baseConfig = new BaseConfig();
        baseGrimmoire = baseConfig.createBaseGrimmoire();

        JPUtils.registerEvents(new PlayerWatcher(this));

        registerGrimmoires();
    }

    @Override
    public void onDisable() {

    }

    private void registerGrimmoires(){
        Grimmoire.registerWrapper(Grimmoire.SAND);
        Grimmoire.registerWrapper(Grimmoire.FIRE);
        Grimmoire.registerWrapper(Grimmoire.ANTI);
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

    public static ItemStack getBaseGrimmoire(){
        return baseGrimmoire.clone();
    }

}
