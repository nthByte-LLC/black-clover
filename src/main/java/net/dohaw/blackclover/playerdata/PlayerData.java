package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.config.PlayerDataConfig;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;

import java.util.UUID;

public class PlayerData {

    @Getter @Setter
    private int maxMana, manaAmount;

    @Getter @Setter
    private GrimmoireWrapper grimmoireWrapper;

    @Getter @Setter
    private PlayerDataConfig config;

    @Getter
    private UUID uuid;

    /*
        For when you're loading the data
     */
    public PlayerData(UUID uuid, GrimmoireWrapper grimmoireWrapper) {
        this.uuid = uuid;
        this.grimmoireWrapper = grimmoireWrapper;
    }

    /*
        For when you're creating data and setting the grimmoire wrapper later.
     */
    public PlayerData(UUID uuid){
        this.uuid = uuid;
    }

}
