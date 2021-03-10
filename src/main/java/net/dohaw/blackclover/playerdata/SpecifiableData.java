package net.dohaw.blackclover.playerdata;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Indicates that a PlayerData class has data that needs to be saved that the regular PlayerData object doesn't take care of. Every class that extends PlayerData has this interface
 */
public interface SpecifiableData {
    void saveSpecifiedData(FileConfiguration config);
    void loadSpecifiedData(FileConfiguration config);

}
