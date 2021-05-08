package net.dohaw.blackclover.grimmoire.spell.type.ash;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.AshPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.SpellUtils;
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
import org.bukkit.scheduler.BukkitTask;

public class AshForm extends CastSpellWrapper implements Listener {

    private double durationInvisiblity;

    public AshForm(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ASH_FORM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        if(pd instanceof AshPlayerData){

            AshPlayerData apd = (AshPlayerData) pd;
            Player player = pd.getPlayer();

            apd.setInAshForm(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (int) (durationInvisiblity * 20), 1, false));

            BukkitTask[] tornadoParticles = SpellUtils.startDoubleTornadoParticles(player, new Particle.DustOptions(BukkitColor.DARK_GREY, 1), new Particle.DustOptions(Color.BLACK, 1), 1);
            pd.addSpellRunnables(KEY, tornadoParticles);

            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                pd.stopSpellRunnables(KEY);
                apd.setInAshForm(false);
            }, (long) (durationInvisiblity * 20));

        }else{
            throw new UnexpectedPlayerData();
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

    @Override
    public void prepareShutdown() {

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
