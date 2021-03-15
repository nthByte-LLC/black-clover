package net.dohaw.blackclover.grimmoire.spell.type.shakudo;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.ShakudoPlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.ResponderFactory;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;

public class WildCall extends CastSpellWrapper {

    private int absorptionAmount;

    public WildCall(GrimmoireConfig grimmoireConfig) {
        super(SpellType.WILD_CALL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        System.out.println("CASTING");
        if(pd instanceof ShakudoPlayerData){

            Player player = pd.getPlayer();
            ShakudoPlayerData spd = (ShakudoPlayerData) pd;
            if(!spd.isSingularWolfSpawned()){

                Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation().add(1, 0, 1), EntityType.WOLF);
                wolf.setOwner(player);
                wolf.setAbsorptionAmount(absorptionAmount);
                wolf.setAdult();
                wolf.setBreed(false);
                SpellUtils.spawnParticle(wolf, Particle.END_ROD, 30, 0.5f, 0.5f, 0.5f);
                SpellUtils.playSound(wolf, Sound.ITEM_CHORUS_FRUIT_TELEPORT);
                spd.setWolf(wolf);
                spd.setSingularWolfSpawned(true);

                if(spd.isPackCalled()){
                    spd.removePack();
                }

            }else{
                if(player.isSneaking()){
                    spd.getWolf().remove();
                    spd.setWolf(null);
                    spd.setSingularWolfSpawned(false);
                }else{
                    ResponderFactory rf = new ResponderFactory(player);
                    rf.sendMessage("You already have a wolf spawned in!");
                }
                return false;
            }

            return true;

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
        this.absorptionAmount = grimmoireConfig.getIntegerSetting(KEY, "Absorption Amount");
    }
}
