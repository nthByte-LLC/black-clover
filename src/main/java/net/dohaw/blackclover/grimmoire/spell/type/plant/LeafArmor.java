package net.dohaw.blackclover.grimmoire.spell.type.plant;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.PersistableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class  LeafArmor extends CastSpellWrapper implements Listener, PersistableSpell {

    private double healingInterval;
    private double duration;
    private int healingAmount;

    private ItemStack helmet, chestplate, leggings, boots;

    // holds the player's uuid and the previous armor contents
    private Map<UUID, ItemStack[]> playersWithArmor = new HashMap<>();

    public LeafArmor(GrimmoireConfig grimmoireConfig) {
        super(SpellType.LEAF_ARMOR, grimmoireConfig);
        createArmor();
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        PlayerInventory inv = player.getInventory();
        ItemStack[] currentArmorContents = inv.getArmorContents();
        playersWithArmor.put(player.getUniqueId(), currentArmorContents);

        inv.setHelmet(helmet);
        inv.setChestplate(chestplate);
        inv.setLeggings(leggings);
        inv.setBoots(boots);

        CircleParticleRunner particleRunner = new CircleParticleRunner(player, new Particle.DustOptions(Color.GREEN, 0.5f), false, 1);
        particleRunner.runTaskTimer(Grimmoire.instance, 0L, 2L);

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            SpellUtils.spawnParticle(player, Particle.VILLAGER_HAPPY, 20, 0.5f, 0.5f, 0.5f);
            SpellUtils.spawnParticle(player, Particle.HEART, 10, 1, 1, 1);
            SpellUtils.playSound(player, Sound.ENTITY_VILLAGER_CELEBRATE);
            double currentHealth = player.getHealth();
            double newHealth = currentHealth + healingAmount;
            double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            player.setHealth(Math.min(newHealth, maxHealth));
        }, 0L, (long) (healingInterval * 20));

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            task.cancel();
            removeArmor(player);
            particleRunner.cancel();
        }, (long) (duration * 20));

        return true;
    }

    private void createArmor(){
        this.helmet = setGreenColor(new ItemStack(Material.LEATHER_HELMET));
        this.chestplate = setGreenColor(new ItemStack(Material.LEATHER_CHESTPLATE));
        this.leggings = setGreenColor(new ItemStack(Material.LEATHER_LEGGINGS));
        this.boots = setGreenColor(new ItemStack(Material.LEATHER_BOOTS));
    }

    private ItemStack setGreenColor(ItemStack armor){
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) armor.getItemMeta();
        armorMeta.setColor(Color.GREEN);
        armor.setItemMeta(armorMeta);
        return armor;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.healingAmount = grimmoireConfig.getIntegerSetting(KEY, "Healing Amount");
        this.healingInterval = grimmoireConfig.getDoubleSetting(KEY, "Healing Interval");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

    @Override
    public void prepareShutdown() {
        for(Map.Entry<UUID, ItemStack[]> entry : playersWithArmor.entrySet()){
            Player player = Bukkit.getPlayer(entry.getKey());
            if(player != null){
                removeArmor(player);
            }
        }
    }

    // Doesn't allow them to remove the armor.
    @EventHandler
    public void onClickArmorSlot(InventoryClickEvent e){

        if(playersWithArmor.containsKey(e.getWhoClicked().getUniqueId())){
            Inventory clickedInventory = e.getClickedInventory();
            if(clickedInventory != null){
                if(clickedInventory instanceof PlayerInventory){
                    if(e.getSlotType() == InventoryType.SlotType.ARMOR){
                        e.setCancelled(true);
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if(playersWithArmor.containsKey(player.getUniqueId())){
            removeArmor(player);
        }
    }

    private void removeArmor(Player player){
        ItemStack[] previousArmorContents = playersWithArmor.remove(player.getUniqueId());
        player.getInventory().setArmorContents(previousArmorContents);
    }

}
