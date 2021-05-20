package net.dohaw.blackclover.grimmoire.spell.type.lightning;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.ThunderstormRunner;
import net.dohaw.blackclover.util.PDCHandler;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

/**
 * Ultimate spell ... Randomly strikes lightning within a specific radius
 */
public class Thunderstorm extends CastSpellWrapper implements Listener {

    public final NamespacedKey STRIKE_OWNER_KEY = NamespacedKey.minecraft("strike_owner");

    private int radius;
    private double damage;
    private int numLightningStrikes;

    public Thunderstorm(GrimmoireConfig grimmoireConfig) {
        super(SpellType.THUNDER_STORM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        System.out.println("Called");
        Player player = pd.getPlayer();
        Grimmoire.LIGHTNING.godSpeed.applyGodspeed(player);
        BukkitTask task = new ThunderstormRunner(player, this).runTaskTimer(Grimmoire.instance, 0L, 10L);
        pd.addSpellRunnables(KEY, task);

        return true;
    }

    @EventHandler
    public void onPlayerTakeLightningDamage(EntityDamageByEntityEvent e){

        Entity eDamaged = e.getEntity();
        if(eDamaged instanceof LivingEntity){

            LivingEntity damagedEntity = (LivingEntity) eDamaged;
            Entity eDamager = e.getDamager();
            if(eDamager instanceof LightningStrike){

                LightningStrike lightningStrike = (LightningStrike) eDamager;
                PersistentDataContainer pdc = lightningStrike.getPersistentDataContainer();
                boolean isPlayerCasted = pdc.has(STRIKE_OWNER_KEY, PersistentDataType.STRING);
                // It's a lightning strike casted by a player/spell
                if(isPlayerCasted){

                    String lightningBoltOwnerName = pdc.get(STRIKE_OWNER_KEY, PersistentDataType.STRING);
                    Player lightningBoltOwner = Bukkit.getPlayer(lightningBoltOwnerName);

                    // Could be null if they're offline for whatever reason
                    if(lightningBoltOwner != null){

                        if(lightningBoltOwner.getUniqueId().equals(eDamaged.getUniqueId())){
                            e.setCancelled(true);
                        }

                        double damagedDone = SpellUtils.callSpellDamageEvent(KEY, damagedEntity, lightningBoltOwner, damage);
                        if(damagedDone != -1){
                            e.setDamage(damagedDone);
                        }

                    }

                }

            }
        }

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getIntegerSetting(KEY, "Radius");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
        this.numLightningStrikes = grimmoireConfig.getIntegerSetting(KEY, "Number Lightning Strikes");
    }

    public int getRadius() {
        return radius;
    }

    public double getDamage() {
        return damage;
    }

    public int getNumLightningStrikes() {
        return numLightningStrikes;
    }

}
