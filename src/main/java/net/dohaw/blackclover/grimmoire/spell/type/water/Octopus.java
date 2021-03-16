package net.dohaw.blackclover.grimmoire.spell.type.water;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.WaterPlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.ResponderFactory;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Octopus extends CastSpellWrapper implements Listener {

    private double duration;

    public Octopus(GrimmoireConfig grimmoireConfig) {
        super(SpellType.OCTOPUS, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        if(pd instanceof WaterPlayerData){
            WaterPlayerData wpd = (WaterPlayerData) pd;
            if(!wpd.isUsingOctopus()){

                TornadoParticleRunner particleRunner = new TornadoParticleRunner(player, new Particle.DustOptions(Color.BLUE, 1), true, 1, false);
                particleRunner.setVerticalPoints(30);
                particleRunner.runTaskTimer(Grimmoire.instance, 0L, 20L);
                SpellUtils.playSound(player, Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED);

                wpd.setUsingOctopus(true);
                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                   particleRunner.cancel();
                   wpd.setUsingOctopus(false);
                }, (long) (duration * 20));

            }else{
                ResponderFactory rf = new ResponderFactory(player);
                rf.sendMessage("You are already using octopus!");
                return false;
            }
        }else{
            try{
                throw new UnexpectedPlayerData();
            }catch(UnexpectedPlayerData event){
                event.printStackTrace();
            }
        }
        return true;
    }

    @EventHandler
    public void onSpellDamage(SpellDamageEvent e){

        Entity eDamaged = e.getDamaged();
        if(eDamaged instanceof Player){

            Player damagedPlayer = (Player) eDamaged;
            Player damagerPlayer = e.getDamager();
            GrimmoireType damagerGrimmoire = Grimmoire.instance.getPlayerDataManager().getData(damagerPlayer.getUniqueId()).getGrimmoireWrapper().getKEY();
            WaterPlayerData damagedPlayerData = (WaterPlayerData) Grimmoire.instance.getPlayerDataManager().getData(damagedPlayer.getUniqueId());

            if(damagedPlayerData.isUsingOctopus()){
                if(damagerGrimmoire != GrimmoireType.ANTI){
                    e.setCancelled(true);
                }
            }

        }

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }
}
