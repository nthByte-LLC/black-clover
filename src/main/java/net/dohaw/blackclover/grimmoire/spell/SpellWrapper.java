package net.dohaw.blackclover.grimmoire.spell;

import lombok.Getter;
import net.dohaw.blackclover.Wrapper;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.ItemStackUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class SpellWrapper extends Wrapper<SpellType> {

    @Getter
    protected int hotbarSlot;

    protected ItemStack spellBoundItem;

    @Getter
    protected double cooldown;

    /**
     * This could be mana or in the Anti grimmoires case, souls
     */
    @Getter
    protected double regenConsumed;


    protected GrimmoireConfig grimmoireConfig;

    public SpellWrapper(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType);
        this.grimmoireConfig = grimmoireConfig;
        loadSettings();
        this.spellBoundItem = createSpellBoundItem();
    }

    public ItemStack getSpellBoundItem(){
        return spellBoundItem.clone();
    }

    public NamespacedKey nsk(){
        return NamespacedKey.minecraft(KEY.getConfigKey() + "_item");
    }

    public boolean isSpellBoundItem(ItemStack stack){
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.has(nsk(), PersistentDataType.STRING);
    }

    public abstract void cast(PlayerData pd);

    public void loadSettings(){
        this.cooldown = grimmoireConfig.getNumberSetting(KEY, "Cooldown");
        this.regenConsumed = grimmoireConfig.getNumberSetting(KEY, "Mana Used");
        this.hotbarSlot = grimmoireConfig.getCustomItemHotbarNum(KEY);
    }

    public ItemStack createSpellBoundItem() {
        return ItemStackUtil.createStack(this, grimmoireConfig.getCustomItemMaterial(KEY), grimmoireConfig.getCustomItemDisplayName(KEY), grimmoireConfig.getCustomItemLore(KEY));
    }

}
