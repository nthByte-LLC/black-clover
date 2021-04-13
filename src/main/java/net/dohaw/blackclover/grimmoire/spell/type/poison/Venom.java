package net.dohaw.blackclover.grimmoire.spell.type.poison;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.VenomBeamRunner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;

public class Venom extends CastSpellWrapper {

    private double beamStayTime;
    private int beamRange;
    private int poisonLevel;
    private double poisonDuration;

    public Venom(GrimmoireConfig grimmoireConfig) {
        super(SpellType.VENOM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        BukkitTask beam = new VenomBeamRunner(player, beamRange, poisonLevel, poisonDuration).runTaskTimer(Grimmoire.instance, 0L, 3L);
        pd.addSpellRunnables(KEY, beam);
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            System.out.println("STOPPING!");
            pd.stopSpellRunnables(KEY);
        }, (long) (beamStayTime * 20));

        return true;
    }

    @Override
    public void prepareShutdown() { }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.beamRange = grimmoireConfig.getIntegerSetting(KEY, "Beam Range");
        this.beamStayTime = grimmoireConfig.getDoubleSetting(KEY, "Beam Stay Time");
        this.poisonDuration = grimmoireConfig.getDoubleSetting(KEY, "Poison Duration");
        this.poisonLevel = grimmoireConfig.getIntegerSetting(KEY, "Poison Level");
    }

}
