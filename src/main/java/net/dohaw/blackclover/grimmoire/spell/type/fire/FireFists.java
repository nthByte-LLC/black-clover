package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.Activatable;
import net.dohaw.blackclover.grimmoire.spell.DamageSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

public class FireFists extends DamageSpellWrapper implements Listener, Activatable {

    protected int fireTicksPerPunch;

    public FireFists(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_FISTS, grimmoireConfig);
        this.fireTicksPerPunch = grimmoireConfig.getNumberSetting(this.getKEY(), "Fire Ticks Per Punch");
    }

    @Override
    public void cast(PlayerData pd) {
        Bukkit.broadcastMessage("CASTING FIRE FISTS");
        activateRunnable(pd);
    }

    @EventHandler
    public void onPlayerStrike(EntityDamageByEntityEvent e){

        Entity eDamager = e.getDamager();
        if(eDamager instanceof Player){

            Player player = (Player) eDamager;
            PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
            if(pd.isSpellActive(this.getKEY())){

                Entity eDamaged = e.getEntity();

                int currentFireTicks = eDamaged.getFireTicks();
                eDamaged.setFireTicks(currentFireTicks + fireTicksPerPunch);

                eDamaged.getWorld().spawnParticle(particle, eDamaged.getLocation(), 10, 1, 1, 1);

            }
        }

    }

    @Override
    public void activateRunnable(PlayerData pd) {

        BukkitTask runnable = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {

            int mana = pd.getManaAmount();
            int newMana = (int) (mana - regenConsumed);
            pd.setManaAmount(newMana);

            Player player = pd.getPlayer();
            World world = player.getWorld();
            world.spawnParticle(particle, player.getLocation(), 10, 1, 1, 1);

        }, 1, 20);

        pd.getActiveSpells().put(this.KEY, runnable);

    }

}
