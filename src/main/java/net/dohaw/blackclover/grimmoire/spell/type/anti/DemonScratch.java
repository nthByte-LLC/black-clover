package net.dohaw.blackclover.grimmoire.spell.type.anti;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.event.SpellOffCooldownEvent;
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
        BukkitTask helix = new HelixParticleRunner(player, new Particle.DustOptions(BukkitColor.darkGrey, 2), 1, false).runTaskTimer(Grimmoire.instance, 0L, 3L);
        BukkitTask helix2 = new HelixParticleRunner(player, new Particle.DustOptions(Color.RED, 2), 1, true).runTaskTimer(Grimmoire.instance, 0L, 3L);
        pd.addActiveSpell(KEY, helix);
        pd.addActiveSpell(KEY, helix2);

        SpellUtils.playSound(player, Sound.BLOCK_BEACON_ACTIVATE);
        deductMana(pd);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            pd.removeActiveSpell(KEY);
        }, (long) (duration * 20));

        return true;
    }

    @EventHandler
    public void onSpellDamage(SpellDamageEvent e){
        Player damager = e.getDamager();
        PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(damager.getUniqueId());
        if(pd.getGrimmoireWrapper().getKEY() == GrimmoireType.ANTI){
            if(pd.isSpellActive(KEY)){
                e.setDamage(e.getDamage() * damageMultiplier);
            }
        }
    }

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
                        e.setDamage(e.getDamage() * damageMultiplier);
                    }
                }
            }

        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.damageMultiplier = grimmoireConfig.getNumberSetting(KEY, "Damage Multiplier");
        this.duration = grimmoireConfig.getNumberSetting(KEY, "Duration");
    }
}
