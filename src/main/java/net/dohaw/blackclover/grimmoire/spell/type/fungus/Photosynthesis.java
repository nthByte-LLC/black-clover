package net.dohaw.blackclover.grimmoire.spell.type.fungus;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.PassiveSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.runnable.spells.PhotosynthesisRunner;
import net.dohaw.corelib.CoreLib;
import org.bukkit.scheduler.BukkitTask;

public class Photosynthesis extends PassiveSpellWrapper {

    private BukkitTask photosynthesisTask;
    private double checkInterval;
    private double healAmount;
    private int requiredLightLevel;

    public Photosynthesis(GrimmoireConfig grimmoireConfig) {
        super(SpellType.PHOTOSYNTHESIS, grimmoireConfig);
        this.photosynthesisTask = new PhotosynthesisRunner(requiredLightLevel, healAmount).runTaskTimer(CoreLib.getInstance(), 0L, (long) (checkInterval * 20));
    }

    @Override
    public void prepareShutdown() {
        photosynthesisTask.cancel();
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.requiredLightLevel = grimmoireConfig.getIntegerSetting(KEY, "Required Light Level");
        this.healAmount = grimmoireConfig.getDoubleSetting(KEY, "Heal Amount");
        this.checkInterval = grimmoireConfig.getDoubleSetting(KEY, "Check Interval");
    }

}
