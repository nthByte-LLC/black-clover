package net.dohaw.blackclover.grimmoire.spell.type;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.PlayerCastSpellEvent;
import net.dohaw.blackclover.grimmoire.spell.DamageSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class FireFists extends DamageSpellWrapper implements Listener {

    public FireFists(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_FISTS, "fire_fists_item", grimmoireConfig);
    }

    @Override
    public void cast(PlayerData pd) {
        Player player = pd.getPlayer();
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(nsk(), PersistentDataType.STRING, " ");
        Bukkit.broadcastMessage("CASTED!");
        Bukkit.getPluginManager().callEvent(new PlayerCastSpellEvent(pd, this));
    }

    @Override
    public void loadSettings() {
        this.damageScale = grimmoireConfig.getNumberSetting(KEY, "Damage Scale");
        this.cooldown = grimmoireConfig.getNumberSetting(KEY, "Cooldown");
        this.regenConsumed = grimmoireConfig.getNumberSetting(KEY, "Mana Used");
        this.hotbarSlot = grimmoireConfig.getCustomItemHotbarNum(KEY);
    }

    @Override
    public ItemStack createSpellBoundItem() {
        return ItemStackUtil.createStack(this, grimmoireConfig.getCustomItemMaterial(KEY), grimmoireConfig.getCustomItemDisplayName(KEY), grimmoireConfig.getCustomItemLore(KEY));
    }

    @EventHandler
    public void onPlayerStrike(EntityDamageByEntityEvent e){

        Entity eDamager = e.getDamager();
        if(eDamager instanceof Player){
            Player player = (Player) eDamager;
            System.out.println("HERE");
        }

    }

}
