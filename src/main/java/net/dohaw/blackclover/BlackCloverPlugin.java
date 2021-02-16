package net.dohaw.blackclover;

import lombok.Getter;
import net.dohaw.blackclover.listener.PlayerWatcher;
import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class BlackCloverPlugin extends JavaPlugin {

    @Getter
    private Map<Integer, Integer> tierGrimmoiresAvailable = new HashMap<>();

    @Override
    public void onEnable() {
        CoreLib.setInstance(this);
        JPUtils.validateFilesOrFolders(
            new HashMap<String, Object>(){{
                put("data", getDataFolder());
            }}
            ,true
        );
        JPUtils.validateFiles("config.yml");
        JPUtils.registerEvents(new PlayerWatcher(this));
    }

    @Override
    public void onDisable() {
    }
}
