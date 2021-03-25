package net.dohaw.blackclover.playerdata;

import lombok.Setter;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import org.bukkit.entity.Snowman;

import java.util.UUID;

public class SnowPlayerData extends PlayerData{

    @Setter
    private Snowman snowman;

    public SnowPlayerData(UUID uuid) {
        super(uuid, Grimmoire.SNOW);
    }

    public boolean isSnowmanSpawned(){
        return snowman != null;
    }

    public void removeSnowman(){
        snowman.remove();
        snowman = null;
    }

}
