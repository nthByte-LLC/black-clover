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
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Armor extends CastSpellWrapper implements DependableSpell, Listener {

    private final NamespacedKey NEEDS_TEMP_ARMOR_REMOVED_KEY = NamespacedKey.minecraft("needs_temp_armor_removed");
    private final NamespacedKey TEMP_ARMOR_MARKER = NamespacedKey.minecraft("temp_armor");

    private double duration;

    public Armor(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ARMOR, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        PlayerInventory inv = player.getInventory();
        giveIronArmor(inv);

        SpellUtils.playSound(player, Sound.BLOCK_ANVIL_PLACE);
        SpellUtils.spawnParticle(player, Particle.TOWN_AURA, 30, 1, 1, 1);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            player.getPersistentDataContainer().set(NEEDS_TEMP_ARMOR_REMOVED_KEY, PersistentDataType.STRING, "marker");
            removeArmor(player);
        }, (long) (duration * 20L));

        return true;
    }

    private void giveIronArmor(PlayerInventory inv) {

            ItemStack helmet = new ItemStack(Material.IRON_HELMET);
            ItemMeta helmetMeta = helmet.getItemMeta();
            helmetMeta.setDisplayName(StringUtils.colorString("&bTemporary Helmet"));
            markArmor(helmetMeta);
            helmet.setItemMeta(helmetMeta);
            ItemStackHelper.addGlowToItem(helmet);
            inv.setHelmet(helmet);

            ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
            ItemMeta chestplateMeta = chestplate.getItemMeta();
            chestplateMeta.setDisplayName(StringUtils.colorString("&bTemporary Chestplate"));
            markArmor(chestplateMeta);
            chestplate.setItemMeta(chestplateMeta);
            ItemStackHelper.addGlowToItem(chestplate);
            inv.setChestplate(chestplate);

            ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
            ItemMeta leggingsMeta = leggings.getItemMeta();
            leggingsMeta.setDisplayName(StringUtils.colorString("&bTemporary Leggings"));
            markArmor(leggingsMeta);
            leggings.setItemMeta(leggingsMeta);
            ItemStackHelper.addGlowToItem(leggings);
            inv.setLeggings(leggings);

            ItemStack boots = new ItemStack(Material.IRON_BOOTS);
            ItemMeta bootsMeta = boots.getItemMeta();
            bootsMeta.setDisplayName(StringUtils.colorString("&bTemporary Boots"));
            markArmor(bootsMeta);
            boots.setItemMeta(bootsMeta);
            ItemStackHelper.addGlowToItem(boots);
            inv.setBoots(boots);

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
                if(player.getPersistentDataContainer().has(NEEDS_TEMP_ARMOR_REMOVED_KEY, PersistentDataType.STRING)){
                    removeArmor(player);
                }
            }
        }, 0L, 20L * 5);
    }

    private void removeArmor(Player player){

        // Removes temp armor from their equipment slots
        EntityEquipment equipment = player.getEquipment();
        if(equipment != null){

            for(EquipmentSlot eSlot : EquipmentSlot.values()){
                ItemStack eSlotItem = equipment.getItem(eSlot);
                if(isTempArmor(eSlotItem)){
                    PersistentDataContainer pdc = player.getPersistentDataContainer();
                    if(pdc.has(NEEDS_TEMP_ARMOR_REMOVED_KEY, PersistentDataType.STRING)){
                        pdc.remove(NEEDS_TEMP_ARMOR_REMOVED_KEY);
                    }
                    equipment.setItem(eSlot, null);
                }
            }

        }

        // Removes temp armor from their regular inventory
        PlayerInventory inv = player.getInventory();
        for(ItemStack stack : inv.getContents()){
            if(isTempArmor(stack)){
                PersistentDataContainer pdc = player.getPersistentDataContainer();
                if(pdc.has(NEEDS_TEMP_ARMOR_REMOVED_KEY, PersistentDataType.STRING)){
                    pdc.remove(NEEDS_TEMP_ARMOR_REMOVED_KEY);
                }
                inv.remove(stack);
            }
        }

    }

    private boolean isTempArmor(ItemStack stack){
        if(stack != null){
            if(stack.getItemMeta() != null){
                return stack.getItemMeta().getPersistentDataContainer().has(TEMP_ARMOR_MARKER, PersistentDataType.STRING);
            }
        }
        return false;
    }

    private void markArmor(ItemMeta meta){
        meta.getPersistentDataContainer().set(TEMP_ARMOR_MARKER, PersistentDataType.STRING, "marker");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        removeArmor(p);
    }

    @Override
    public void initDependableData() {
        startTemporaryArmorRemover();
    }

}
