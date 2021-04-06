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

                TornadoParticleRunner particleRunner = new TornadoParticleRunner(player, new Particle.DustOptions(Color.BLUE, 1), false, 1.5, false);
                TornadoParticleRunner particleRunner1 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.WHITE, 1), false, 1.5, true);
                wpd.addSpellRunnables(KEY, particleRunner1.runTaskTimer(Grimmoire.instance, 0L, 1L), particleRunner.runTaskTimer(Grimmoire.instance, 0L, 1L));

                SpellUtils.playSound(player, Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED);

                wpd.setUsingOctopus(true);
                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                   wpd.stopSpellRunnables(KEY);
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

    /*
        If the player is using Octopus, they cancel all damage except if it's to an anti spell.
     */
    @EventHandler
    public void onSpellDamage(SpellDamageEvent e){

        Entity eDamaged = e.getDamaged();
        if(eDamaged instanceof Player){

            Player damagedPlayer = (Player) eDamaged;
            Player damagerPlayer = e.getDamager();
            PlayerData damagedPlayerData = Grimmoire.instance.getPlayerDataManager().getData(damagedPlayer.getUniqueId());

            if(damagedPlayerData instanceof WaterPlayerData){
                WaterPlayerData wpd = (WaterPlayerData) damagedPlayerData;
                if(wpd.isUsingOctopus()){
                    GrimmoireType damagerGrimmoire = Grimmoire.instance.getPlayerDataManager().getData(damagerPlayer.getUniqueId()).getGrimmoireWrapper().getKEY();
                    if(damagerGrimmoire != GrimmoireType.ANTI){
                        e.setCancelled(true);
                    }
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
