package net.dohaw.blackclover;

public enum XPGainType {

    MOB_KILL("Mob Kill"),
    PLAYER_KILL("Player Kill"),
    SPELL_CAST("Spell Cast");

    private String configKey;
    XPGainType(String configKey){
        this.configKey = configKey;
    }

    public String getConfigKey() {
        return configKey;
    }

}
