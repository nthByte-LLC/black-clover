package net.dohaw.blackclover.grimmoire.spell.type.sand;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SandStorm extends CastSpellWrapper {

    private int blindnessDuration;
    private int distance;

    public SandStorm(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SAND_STORM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        for(Entity en : player.getNearbyEntities(distance, distance, distance)){
            if(en instanceof LivingEntity){
                LivingEntity le = (LivingEntity) en;
                le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, blindnessDuration * 20, 1, false));
                SpellUtils.spawnParticle(le, Particle.TOTEM, 30, 1, 1, 1);
            }
        }
        TornadoParticleRunner runner = new TornadoParticleRunner(player, new Particle.DustOptions(Color.YELLOW, 1), 1, true);
        TornadoParticleRunner runner2 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.GRAY, 1), 1, false);
        runner.runTaskTimer(Grimmoire.instance, 0L, 1L);
        runner2.runTaskTimer(Grimmoire.instance, 0L, 1L);
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            runner.cancel();
            runner2.cancel();
        }, 30);

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.distance = grimmoireConfig.getIntegerSetting(KEY, "Distance");
        this.blindnessDuration = grimmoireConfig.getIntegerSetting(KEY, "Blindness Duration");
    }

    @Override
    public void prepareShutdown() {

    }
}
