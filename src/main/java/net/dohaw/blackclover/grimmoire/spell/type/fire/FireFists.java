package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DamageableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireFists extends ActivatableSpellWrapper implements Listener, DamageableSpell {

    protected int fireTicksPerPunch;

    public FireFists(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_FISTS, grimmoireConfig);
        this.fireTicksPerPunch = grimmoireConfig.getNumberSetting(this.getKEY(), "Fire Ticks Per Punch");
    }

    @EventHandler
    public void onPlayerStrike(EntityDamageByEntityEvent e){

        Entity eDamager = e.getDamager();
        if(eDamager instanceof Player){

            Player player = (Player) eDamager;
            PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
            if(pd.isSpellActive(this.getKEY())){

                SpellDamageEvent event = new SpellDamageEvent(KEY, e.getEntity(), player);
                Bukkit.getPluginManager().callEvent(event);

                if(!event.isCancelled()){

                    Entity eDamaged = e.getEntity();

                    int currentFireTicks = eDamaged.getFireTicks();

                    if(currentFireTicks < 0){
                        currentFireTicks = 0;
                    }

                    eDamaged.setFireTicks(currentFireTicks + fireTicksPerPunch);

                    eDamaged.getWorld().spawnParticle(particle, eDamaged.getLocation(), 30, 1, 1, 1);

                    if(damageScale != 1){
                        e.setDamage(e.getDamage() * damageScale);
                    }

                }else{
                    // Cancels regular punch damage
                    e.setCancelled(true);
                }

            }
        }

    }

    public void doRunnableSpecifics(PlayerData pd){
        Player player = pd.getPlayer();
        World world = player.getWorld();
        world.spawnParticle(particle, player.getLocation(), 30, 1, 1, 1);
        SpellUtils.playSound(player, Sound.BLOCK_FIRE_AMBIENT);
    }

}
