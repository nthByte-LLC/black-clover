package net.dohaw.blackclover.grimmoire.spell;

import lombok.Getter;
import lombok.NonNull;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.event.Event;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

public abstract class CastSpellWrapper extends SpellWrapper{

    @Getter
    protected int hotbarSlot;

    @Getter
    protected double cooldown;

    /**
     * This could be mana or in the Anti grimmoires case, souls
     */
    @Getter
    protected double regenConsumed;

    public CastSpellWrapper(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    public boolean isSpellBound(@NonNull PersistentDataHolder pdh){
        PersistentDataContainer pdc = pdh.getPersistentDataContainer();
        return pdc.has(nsk(), PersistentDataType.STRING);
    }

    public boolean isSpellBoundSlot(int slot){
        return this.getHotbarSlot() == slot;
    }

    public void markAsSpellBinding(@NonNull PersistentDataHolder pdh){
        PersistentDataContainer pdc = pdh.getPersistentDataContainer();
        pdc.set(nsk(), PersistentDataType.STRING, "mark");
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.regenConsumed = grimmoireConfig.getNumberSetting(KEY, "Mana Used");
        this.cooldown = grimmoireConfig.getNumberSetting(KEY, "Cooldown");
        this.hotbarSlot = grimmoireConfig.getNumberSetting(KEY, "Hotbar Number");
    }

    public void deductMana(PlayerData pd){
        pd.setManaAmount((int) (pd.getManaAmount() - regenConsumed));
    }

    public abstract boolean cast(Event e, PlayerData pd);

}