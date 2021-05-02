package net.dohaw.blackclover.grimmoire.spell.type.cotton;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.type.Cotton;
import net.dohaw.blackclover.playerdata.CottonPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Sheep extends CastSpellWrapper {

    public Sheep(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SHEEP, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        if(pd instanceof CottonPlayerData){

            Player player = pd.getPlayer();
            CottonPlayerData cpd = (CottonPlayerData) pd;
            if(!cpd.isSheepSpawned()){

                Location sheepSpawn = LocationUtil.getLocationInFront(player, 1).add(0, 1, 0);
                org.bukkit.entity.Sheep sheep = (org.bukkit.entity.Sheep) player.getWorld().spawnEntity(sheepSpawn, EntityType.SHEEP);
                cpd.setSingleSheep(sheep);

                SpellUtils.playSound(sheepSpawn, Sound.ITEM_CHORUS_FRUIT_TELEPORT);
                SpellUtils.spawnParticle(sheepSpawn, Particle.END_ROD, 10, 1, 1, 1);
                return true;

            }else{
                if(player.isSneaking()){
                    cpd.removeSheep();
                }else{
                    player.sendMessage("You already have a sheep spawned!");
                }
            }

        }else{
            throw new UnexpectedPlayerData();
        }

        return false;
    }

    @Override
    public void prepareShutdown() {

    }
}
