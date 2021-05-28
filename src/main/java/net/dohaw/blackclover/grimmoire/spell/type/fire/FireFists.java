package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireFists extends ActivatableSpellWrapper implements Listener {

    private double damageScale;
    protected int fireTicksPerPunch;

    public FireFists(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_FISTS, grimmoireConfig);
    }

    @EventHandler
    public void onPlayerStrike(EntityDamageByEntityEvent e){

        Entity eDamager = e.getDamager();
        Entity damagedEntity = e.getEntity();
        if(eDamager instanceof Player && damagedEntity instanceof LivingEntity){

            Player player = (Player) eDamager;
            PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
            if(pd.isSpellActive(KEY)){

                double dmg = e.getDamage() * damageScale;
                double damageDone = SpellUtils.callSpellDamageEvent(KEY, (LivingEntity) damagedEntity, player, dmg);

                if(damageDone != SpellUtils.DAMAGE_CANCEL_VALUE){

                    Entity eDamaged = e.getEntity();
                    e.setDamage(damageDone);

                    int currentFireTicks = eDamaged.getFireTicks();
                    if(currentFireTicks < 0){
                        currentFireTicks = 0;
                    }

                    eDamaged.setFireTicks(currentFireTicks + fireTicksPerPunch);
                    SpellUtils.spawnParticle(eDamaged, Particle.FLAME, 30, 1, 1, 1);

                }else{
                    System.out.println("CANCELED");
                    // Cancels regular punch damage
                    e.setCancelled(true);
                }

            }
        }

    }

    public void doRunnableTick(PlayerData pd){
        Player player = pd.getPlayer();
        SpellUtils.spawnParticle(player, Particle.FLAME, 30, 1, 1, 1);
        SpellUtils.playSound(player, Sound.BLOCK_FIRE_AMBIENT);
    }

    @Override
    public void deactiveSpell(PlayerData caster) {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.damageScale = grimmoireConfig.getDoubleSetting(KEY, "Damage Scale");
        this.fireTicksPerPunch = grimmoireConfig.getIntegerSetting(this.getKEY(), "Fire Ticks Per Punch");
    }

}
