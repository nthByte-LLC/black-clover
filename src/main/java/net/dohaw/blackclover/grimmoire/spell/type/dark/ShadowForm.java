package net.dohaw.blackclover.grimmoire.spell.type.dark;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ShadowForm extends CastSpellWrapper {

    private double duration;

    public ShadowForm(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SHADOW_FORM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();
        player.setInvisible(true);
        pd.setInVulnerable(true);

        BukkitTask particleSpawner = new BukkitRunnable(){
            @Override
            public void run() {
                SpellUtils.spawnParticle(player.getLocation(), Particle.REDSTONE, new Particle.DustOptions(Color.BLACK, 2), 10, 0.5f, 0.5f, 0.5f);
            }
        }.runTaskTimer(Grimmoire.instance, 0L, 10L);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
           particleSpawner.cancel();
           pd.setInVulnerable(false);
           player.setInvisible(false);
        }, (long) duration * 20);

        return true;
    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }
}
