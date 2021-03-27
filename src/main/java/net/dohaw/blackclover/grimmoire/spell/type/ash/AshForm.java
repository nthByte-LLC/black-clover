package net.dohaw.blackclover.grimmoire.spell.type.ash;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.AshPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AshForm extends CastSpellWrapper implements Listener {

    private double durationInvisiblity;

    public AshForm(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ASH_FORM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        if(pd instanceof AshPlayerData){

            AshPlayerData apd = (AshPlayerData) pd;
            Player player = pd.getPlayer();

            apd.setInAshForm(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (int) (durationInvisiblity * 20), 1, false));

            TornadoParticleRunner pr1 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.BLACK, 1), true, 1, false);
            TornadoParticleRunner pr2 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.BLACK, 1), true, 1, true);
            pd.addSpellRunnable(KEY, pr1.runTaskTimer(Grimmoire.instance, 0L, 1L), pr2.runTaskTimer(Grimmoire.instance, 0L, 1L));

            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                System.out.println("HERE");
               pd.stopSpellRunnables(KEY);
               apd.setInAshForm(false);
            }, (long) (durationInvisiblity * 20));

        }else{
            try {
                throw new UnexpectedPlayerData();
            } catch (UnexpectedPlayerData unexpectedPlayerData) {
                unexpectedPlayerData.printStackTrace();
            }
            return false;
        }

        return true;
    }

    /*
        Makes it to where their spells don't do damage while in ash form.
     */
//    @EventHandler
//    public void onSpellDamage(SpellDamageEvent e){
//        Player damager = e.getDamager();
//        ashFormCheck(damager, e);
//    }

    /*
        Makes it to where their hits or projectiles don't do damage while in ash form
     */
    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent e){

        Entity eDamager = e.getDamager();
        Player damager = null;
        if(eDamager instanceof Player){
            damager = (Player)eDamager;
        } else if(eDamager instanceof Projectile) {
            Projectile proj = (Projectile) eDamager;
            if(proj.getShooter() instanceof Player){
                damager = (Player) proj.getShooter();
            }
        }

        if(damager != null){
            ashFormCheck(damager, e);
        }

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.durationInvisiblity = grimmoireConfig.getDoubleSetting(KEY, "Duration Invisibility");
    }

    private void ashFormCheck(Player damager, Cancellable e){
        PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(damager.getUniqueId());
        if(pd.getGrimmoireWrapper().getKEY() == GrimmoireType.ASH){
            AshPlayerData apd = (AshPlayerData) pd;
            if(apd.isInAshForm()){
                e.setCancelled(true);
            }
        }
    }

}