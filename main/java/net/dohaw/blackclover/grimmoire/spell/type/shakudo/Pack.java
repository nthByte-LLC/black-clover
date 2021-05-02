package net.dohaw.blackclover.grimmoire.spell.type.shakudo;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.ShakudoPlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.ResponderFactory;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class Pack extends CastSpellWrapper {

    private int absorptionAmount;
    private int numWolves;

    public Pack(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PACK, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        if(pd instanceof ShakudoPlayerData){
            ShakudoPlayerData spd = (ShakudoPlayerData) pd;
            Player player = pd.getPlayer();
            if(!spd.isPackCalled()){

                spd.setPackCalled(true);

                // block in front
                Location wolfLocation = player.getLocation().add(player.getLocation().getDirection().multiply(1));
                // so that they don't spawn straight into the ground
                wolfLocation = wolfLocation.add(0, 1,0);

                SpellUtils.spawnParticle(wolfLocation, Particle.END_ROD, 30, 0.5f, 0.5f, 0.5f);
                SpellUtils.playSound(wolfLocation, Sound.ITEM_CHORUS_FRUIT_TELEPORT);

                List<Wolf> pack = new ArrayList<>();
                for (int i = 0; i < numWolves; i++) {
                    Wolf wolf = (Wolf) player.getWorld().spawnEntity(wolfLocation, EntityType.WOLF);
                    wolf.setOwner(player);
                    wolf.setAbsorptionAmount(absorptionAmount);
                    wolf.setAdult();
                    wolf.setBreed(false);
                    pack.add(wolf);
                }

                spd.setPack(pack);

                if(spd.isSingularWolfSpawned()){
                    Wolf singularWolf = spd.getWolf();
                    singularWolf.remove();
                    spd.setSingularWolfSpawned(false);
                }

                return true;

            }else{
                if(player.isSneaking()){
                    spd.removePack();
                }else{
                    ResponderFactory rf = new ResponderFactory(player);
                    rf.sendMessage("You already have called the pack!");
                }
                return false;
            }
        }else{
            try {
                throw new UnexpectedPlayerData();
            } catch (UnexpectedPlayerData unexpectedPlayerData) {
                unexpectedPlayerData.printStackTrace();
            }
            return false;
        }

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.numWolves = grimmoireConfig.getIntegerSetting(KEY, "Number of Wolves");
        this.absorptionAmount = grimmoireConfig.getIntegerSetting(KEY, "Wolf Absorption Amount");
    }

    @Override
    public void prepareShutdown() {

    }

}
