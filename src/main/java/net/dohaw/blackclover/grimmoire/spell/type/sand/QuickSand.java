package net.dohaw.blackclover.grimmoire.spell.type.sand;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class QuickSand extends CastSpellWrapper {

    private int slownessLevel;
    private int slownessDuration;
    private double distance;

    public QuickSand(GrimmoireConfig grimmoireConfig) {
        super(SpellType.QUICK_SAND, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        for(Entity en : player.getNearbyEntities(distance, distance, distance)){
            if(en instanceof LivingEntity){
                LivingEntity le = (LivingEntity) en;
                if(!le.getUniqueId().equals(player.getUniqueId())){
                    le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, slownessDuration * 20, slownessLevel, false));
                    SpellUtils.spawnParticle(le, Particle.SQUID_INK, 30, 0.5f, 0.5f, 0.5f);
                    SpellUtils.playSound(le, Sound.ENTITY_SQUID_SQUIRT);
                }
            }
        }

        SpellUtils.spawnParticle(player, Particle.FALLING_DUST, Material.BONE_BLOCK.createBlockData(), 30, 1, 1, 1);

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.distance = grimmoireConfig.getNumberSetting(KEY, "Distance");
        this.slownessDuration = (int) grimmoireConfig.getNumberSetting(KEY, "Slowness Duration");
        this.slownessLevel = (int) (grimmoireConfig.getNumberSetting(KEY, "Slowness Level") - 1);
    }

}
