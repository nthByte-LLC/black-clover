package net.dohaw.blackclover.grimmoire.spell.type.snow;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.TimeCastable;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class Freeze extends CastSpellWrapper implements TimeCastable {

    private double invulnerableDuration, freezeDuration, castTime, radius;

    public Freeze(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FREEZE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();

        TornadoParticleRunner pr1 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.WHITE, 0.5f), true, 1, false);
        TornadoParticleRunner pr2 = new TornadoParticleRunner(player, new Particle.DustOptions(Color.GRAY, 0.5f), true, 1, true);
        pd.addSpellRunnable(KEY, pr1.runTaskTimer(Grimmoire.instance, 0L, 2L), pr2.runTaskTimer(Grimmoire.instance, 0L, 2L));

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            pd.stopTimedCast();

        }, (long) (castTime * 20));

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castTime = grimmoireConfig.getDoubleSetting(KEY, "Cast Time");
        this.radius = grimmoireConfig.getDoubleSetting(KEY, "Radius");
        this.invulnerableDuration = grimmoireConfig.getDoubleSetting(KEY, "Invulnerable Duration");
        this.freezeDuration = grimmoireConfig.getDoubleSetting(KEY, "Freeze Duration");
    }
}
