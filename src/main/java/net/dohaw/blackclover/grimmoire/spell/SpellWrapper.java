package net.dohaw.blackclover.grimmoire.spell;

import lombok.Getter;
import net.dohaw.blackclover.Wrapper;
import net.dohaw.blackclover.config.GrimmoireConfig;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;

public abstract class SpellWrapper extends Wrapper<SpellType> {


    @Getter
    protected Particle particle;

    protected GrimmoireConfig grimmoireConfig;

    public SpellWrapper(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType);
        this.grimmoireConfig = grimmoireConfig;
        loadSettings();
    }

    public NamespacedKey nsk(){
        return NamespacedKey.minecraft(KEY.getConfigKey());
    }

    public void loadSettings(){ }

    public abstract void prepareShutdown();

}
