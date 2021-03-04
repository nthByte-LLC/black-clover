package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.Activatable;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DamageableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

public class FireFists extends CastSpellWrapper implements Listener, Activatable, DamageableSpell {

    protected int fireTicksPerPunch;

    public FireFists(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_FISTS, grimmoireConfig);
        this.fireTicksPerPunch = grimmoireConfig.getNumberSetting(this.getKEY(), "Fire Ticks Per Punch");
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        activateRunnable(pd);
        return true;
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

    @Override
    public void activateRunnable(PlayerData pd) {

        BukkitTask runnable = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {

            BlackCloverPlugin instance = Grimmoire.instance;
            PlayerData updatedData = instance.getPlayerDataManager().getData(pd.getUuid());

            if(updatedData.hasSufficientRegenForSpell(this)){

                int mana = updatedData.getRegenAmount();
                int newMana = (int) (mana - regenConsumed);
                updatedData.setRegenAmount(newMana);
                instance.updateRegenBar(updatedData);

                Player player = updatedData.getPlayer();
                World world = player.getWorld();
                world.spawnParticle(particle, player.getLocation(), 30, 1, 1, 1);

            }else{
                updatedData.removeActiveSpell(this.KEY);
            }

        }, 1, 20);

        PlayerData updatedData = Grimmoire.instance.getPlayerDataManager().getData(pd.getUuid());
        updatedData.getActiveSpells().put(this.KEY, runnable);

    }

    @Override
    public int getDuration() {
        return 0;
    }

}
