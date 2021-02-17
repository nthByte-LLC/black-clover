package net.dohaw.blackclover.config;

import net.dohaw.corelib.Config;

public class GrimmoireConfig extends Config {

    public GrimmoireConfig(String fileName) {
        super(fileName);
    }

    public String getDisplayNameColor(){
        return config.getString("Display Name Color");
    }

}
