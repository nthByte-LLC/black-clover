package net.dohaw.blackclover.grimmoire.spell.type.cotton;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.type.Cotton;
import net.dohaw.blackclover.playerdata.CottonPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;

public class Sheep extends CastSpellWrapper {

    public Sheep(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SHEEP, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        if(pd instanceof CottonPlayerData){

            CottonPlayerData cpd = (CottonPlayerData) pd;
            if(cpd.isSheepSpawned()){

            }

        }else{
            throw new UnexpectedPlayerData();
        }

        return false;
    }
}
