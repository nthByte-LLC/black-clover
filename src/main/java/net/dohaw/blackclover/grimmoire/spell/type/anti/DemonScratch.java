package net.dohaw.blackclover.grimmoire.spell.type.anti;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.HelixParticleRunner;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitTask;

public class DemonScratch extends CastSpellWrapper implements Listener {

    private double duration;
    private double damageMultiplier;

    /*
        Different from an ActivatableSpellWrapper because they take for every second it is used. This is a 1 time cast thing but the effects of demon scratch linger for however the duration is.
     */
    public DemonScratch(GrimmoireConfig grimmoireConfig) {
        super(SpellType.DEMON_SCRATCH, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        BukkitTask helix = new HelixParticleRunner(player, new Particle.DustOptions(BukkitColor.DARK_GREY, 2), 1, false).runTaskTimer(Grimmoire.instance, 0L, 3L);
        BukkitTask helix2 = new HelixParticleRunner(player, new Particle.DustOptions(Color.RED, 2), 1, true).runTaskTimer(Grimmoire.instance, 0L, 3L);
        pd.addSpellRunnables(KEY, helix, helix2);

        SpellUtils.playSound(player, Sound.BLOCK_BEACON_ACTIVATE);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            pd.stopSpellRunnables(KEY);
        }, (long) (duration * 20));

        return true;
    }

    /*
        Doubles any anti spell damage. (Currently no anti spells do any damage, but this is future proofing.)
     */
    @EventHandler
    public void onSpellDamage(SpellDamageEvent e){
        Player damager = e.getDamager();
        PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(damager.getUniqueId());
        if(e.getSpell() != KEY){
            if(pd.getGrimmoireWrapper().getKEY() == GrimmoireType.ANTI){
                if(pd.isSpellActive(KEY)){
                    e.setDamage(e.getDamage() * damageMultiplier);
                    System.out.println("DOUBLE THE DAMAGE2");
                }
            }
        }
    }

    /*
        Whenever you do non-spell damage to someone (i.e. with a sweeping attack or bow) it'll increase your damage
     */
    @EventHandler
    public void onDoDamage(EntityDamageByEntityEvent e){

        Entity eDamager = e.getDamager();
        Entity eDamaged = e.getEntity();
        if(eDamaged instanceof Player){

            Player damager = null;
            // Gets the player object from projectiles as well if applicable
            if(eDamager instanceof Projectile){
                ProjectileSource projSource = ((Projectile) eDamager).getShooter();
                if(projSource instanceof Player){
                    damager = (Player) projSource;
                }
            }else if(eDamager instanceof Player){
                damager = (Player) eDamager;
            }

            if(damager != null){
                PlayerData damagerData = Grimmoire.instance.getPlayerDataManager().getData(damager.getUniqueId());
                if(damagerData.getGrimmoireWrapper().getKEY() == GrimmoireType.ANTI){
                    if(damagerData.isSpellActive(KEY)){

                        double newDamage = e.getDamage() * damageMultiplier;
                        SpellDamageEvent spellDamageEvent = new SpellDamageEvent(KEY, newDamage, eDamaged, damager);
                        Bukkit.getPluginManager().callEvent(spellDamageEvent);
                        if(!spellDamageEvent.isCancelled()){
                            e.setDamage(e.getDamage() * damageMultiplier);
                            System.out.println("DOUBLE THE DAMAGE");
                        }

                    }
                }
            }

        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.damageMultiplier = grimmoireConfig.getIntegerSetting(KEY, "Damage Multiplier");
        this.duration = grimmoireConfig.getIntegerSetting(KEY, "Duration");
    }

}
