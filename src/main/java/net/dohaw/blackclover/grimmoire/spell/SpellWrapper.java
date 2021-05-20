package net.dohaw.blackclover.grimmoire.spell;

import net.dohaw.blackclover.Wrapper;
import net.dohaw.blackclover.config.GrimmoireConfig;
import org.bukkit.NamespacedKey;

public abstract class SpellWrapper extends Wrapper<SpellType> {

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

}
