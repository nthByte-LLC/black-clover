package net.dohaw.blackclover.grimmoire.spell.type.iron;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DependableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.StringUtils;
import net.dohaw.corelib.helpers.ItemStackHelper;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Armor extends CastSpellWrapper implements DependableSpell, Listener {
    private double duration;

    public Armor(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ARMOR, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        PlayerInventory inv = player.getInventory();
        setIron(inv);
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 10, 10);
        player.getWorld().spawnParticle(Particle.TOWN_AURA, player.getLocation(), 1);
        SpellUtils.spawnParticle(player, Particle.TOWN_AURA, 30, 1, 1, 1);
        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            player.getPersistentDataContainer().set(NamespacedKey.minecraft("needs_temp_armor_removed"), PersistentDataType.STRING, "marker");
            removePotentialTempSword(player);
        }, (long) (duration * 20L));

        return true;
    }

    public static void setIron(PlayerInventory inv) {
        {
            ItemStack item = new ItemStack(Material.IRON_HELMET);
            ItemMeta mitem = item.getItemMeta();
            mitem.setDisplayName(StringUtils.colorString("&bTemporary Helmet"));
            mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("temp_armor"), PersistentDataType.STRING, "marker");
            item.setItemMeta(mitem);
            ItemStackHelper.addGlowToItem(item);
            inv.setHelmet(item);
        }
        {
            ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
            ItemMeta mitem = item.getItemMeta();
            mitem.setDisplayName(StringUtils.colorString("&bTemporary Chestplate"));
            mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("temp_armor"), PersistentDataType.STRING, "marker");
            item.setItemMeta(mitem);
            ItemStackHelper.addGlowToItem(item);
            inv.setChestplate(item);
        }
        {
            ItemStack item = new ItemStack(Material.IRON_LEGGINGS);
            ItemMeta mitem = item.getItemMeta();
            mitem.setDisplayName(StringUtils.colorString("&bTemporary Leggings"));
            mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("temp_armor"), PersistentDataType.STRING, "marker");
            item.setItemMeta(mitem);
            ItemStackHelper.addGlowToItem(item);
            inv.setLeggings(item);
        }
        {
            ItemStack item = new ItemStack(Material.IRON_BOOTS);
            ItemMeta mitem = item.getItemMeta();
            mitem.setDisplayName(StringUtils.colorString("&bTemporary Boots"));
            mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("temp_armor"), PersistentDataType.STRING, "marker");
            item.setItemMeta(mitem);
            ItemStackHelper.addGlowToItem(item);
            inv.setBoots(item);
        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

    @Override
    public void prepareShutdown() {}

    private void startTemporaryArmorRemover(){
        Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            for(Player player : Bukkit.getOnlinePlayers()){
                if(player.getPersistentDataContainer().has(NamespacedKey.minecraft("needs_temp_armor_removed"), PersistentDataType.STRING)){
                    removePotentialTempSword(player);
                }
            }
        }, 0L, 20L * 5);
    }

    private void removePotentialTempSword(Player player){
        PlayerInventory inv = player.getInventory();
        for(ItemStack stack : inv.getContents()){
            if(stack != null && stack.getItemMeta() != null){
                boolean isTempSword = stack.getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("needs_temp_armor_removed"), PersistentDataType.STRING);
                if(isTempSword){
                    PersistentDataContainer pdc = player.getPersistentDataContainer();
                    if(pdc.has(NamespacedKey.minecraft("needs_temp_armor_removed"), PersistentDataType.STRING)){
                        pdc.remove(NamespacedKey.minecraft("needs_temp_armor_removed"));
                    }
                    inv.remove(stack);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        removePotentialTempSword(e.getPlayer());
    }

    @Override
    public void initDependableData() {
        startTemporaryArmorRemover();
    }
}
