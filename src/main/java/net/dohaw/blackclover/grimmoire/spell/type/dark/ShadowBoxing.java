package net.dohaw.blackclover.grimmoire.spell.type.dark;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.DarkPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ShadowBoxing extends ActivatableSpellWrapper implements Listener {

    private double damage;
    private int witherLevel;
    private double witherDuration;

    public ShadowBoxing(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SHADOW_BOXING, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        ((DarkPlayerData)pd).setShadowBoxing(true);
        return super.cast(e, pd);
    }

    @Override
    public void doRunnableTick(PlayerData caster) {
        Player casterPlayer = caster.getPlayer();
        SpellUtils.spawnParticle(casterPlayer.getLocation(), Particle.DRIPPING_OBSIDIAN_TEAR,30, 1, 1, 1);
        SpellUtils.playSound(casterPlayer, Sound.BLOCK_SAND_FALL);
    }

    @Override
    public void deactiveSpell(PlayerData caster) {
        ((DarkPlayerData)caster).setShadowBoxing(false);
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.witherDuration = grimmoireConfig.getDoubleSetting(KEY, "Wither Duration");
        this.witherLevel = grimmoireConfig.getIntegerSetting(KEY, "Wither Level");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
    }

    @EventHandler
    public void onPlayerHitEntity(EntityDamageByEntityEvent e){

        Entity eDamaged = e.getEntity();
        Entity eDamager = e.getDamager();
        if(eDamager instanceof Player && eDamaged instanceof LivingEntity){

            LivingEntity damaged = (LivingEntity) eDamaged;
            Player damager = (Player) eDamager;
            PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(damager);
            if(pd instanceof DarkPlayerData){

                DarkPlayerData darkPlayerData = (DarkPlayerData) pd;
                if(darkPlayerData.isShadowBoxing()){
                    double damageDone = SpellUtils.callSpellDamageEvent(KEY, damaged, damager, damage);
                    if(damageDone != SpellUtils.DAMAGE_CANCEL_VALUE){
                        e.setDamage(damage);
                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (int) witherDuration * 20, witherLevel - 1));
                    }
                }

            }

        }

    }

}
