package net.dohaw.blackclover.grimmoire.spell.type.lightning;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.DependableSpell;
import net.dohaw.blackclover.grimmoire.spell.PassiveSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Makes a ball of flame in the player's hand constantly. Every time they punch something, it catches on fire (living entities)
 */
public class ElectricFire extends PassiveSpellWrapper implements DependableSpell, Listener {

    private int fireTicksPerHit;
    private double damageMultiplier;

    public ElectricFire(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ELECTRIC_FIRE, grimmoireConfig);
    }

    @EventHandler
    public void onHitEntity(EntityDamageByEntityEvent e){

        Entity eDamaged = e.getEntity();
        Entity eDamager = e.getDamager();

        if(eDamager instanceof Player && eDamaged instanceof LivingEntity){

            Player damager = (Player) eDamager;
            PlayerData playerData = Grimmoire.instance.getPlayerDataManager().getData(damager.getUniqueId());
            if(playerData.getGrimmoireType() == GrimmoireType.LIGHTNING){

                double initDamage = e.getDamage();
                double damage = initDamage * damageMultiplier;
                LivingEntity le = (LivingEntity) eDamaged;
                double damagedDone = SpellUtils.callSpellDamageEvent(KEY, le, (Player) eDamager, damage);
                if(damagedDone != -1){

                    int currentFireTicks = le.getFireTicks();
                    int newFireTicks = currentFireTicks + fireTicksPerHit;
                    int maxFireTicks = le.getMaxFireTicks();
                    if(newFireTicks < maxFireTicks){
                        newFireTicks = maxFireTicks;
                    }

                    le.setFireTicks(newFireTicks);
                    e.setDamage(damagedDone);
                    SpellUtils.spawnParticle(le, Particle.SOUL_FIRE_FLAME, 10, 0.5f,0.5f, 0.5f);

                }

            }

        }

    }

    @Override
    public void initDependableData() {
        Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            for(Player player : Bukkit.getOnlinePlayers()){
                PlayerData data = Grimmoire.instance.getPlayerDataManager().getData(player);
                if(data.getGrimmoireType() == GrimmoireType.LIGHTNING){
                    double offsetToRight = 0.5;
                    double offsetForward = 0.4;
                    Location elevatedLocation = player.getLocation().add(0, 1, 0);
                    Location electricFireLocation = LocationUtil.getAbsoluteLocationToRight(LocationUtil.getAbsoluteLocationInFront(elevatedLocation, offsetForward), offsetToRight);
                    SpellUtils.spawnParticle(electricFireLocation, Particle.REDSTONE, new Particle.DustOptions(BukkitColor.PALE_CYAN, 1), 30, 0, 0, 0);
                }
            }
        },0L, 5L);
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.damageMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Damage Multiplier");
        this.fireTicksPerHit = grimmoireConfig.getIntegerSetting(KEY, "Fire Ticks Per Hit");
    }

}
