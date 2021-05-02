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

public class Sword extends CastSpellWrapper implements DependableSpell, Listener {

    private final NamespacedKey TEMP_SWORD_MARKER = NamespacedKey.minecraft("temp-sword-marker");
    private final NamespacedKey NEEDS_TEMP_SWORD_REMOVED = NamespacedKey.minecraft("needs-temp-sword-removed");

    private double duration;

    public Sword(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SWORD, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(StringUtils.colorString("&bTemporary Sword"));
        meta.getPersistentDataContainer().set(TEMP_SWORD_MARKER, PersistentDataType.STRING, "marker");
        sword.setItemMeta(meta);
        ItemStackHelper.addGlowToItem(sword);

        Player player = pd.getPlayer();
        PlayerInventory inv = player.getInventory();
        inv.addItem(sword);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            // Remove the sword from the player's inventory
            player.getPersistentDataContainer().set(NEEDS_TEMP_SWORD_REMOVED, PersistentDataType.STRING, "marker");
            removePotentialTempSword(player);
        }, (long) (duration * 20L));

        SpellUtils.playSound(player, Sound.BLOCK_ANVIL_PLACE);
        SpellUtils.spawnParticle(player, Particle.TOWN_AURA, 30, 1, 1, 1);

        return true;
    }

    @Override
    public void prepareShutdown() { }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

    private void startTemporarySwordRemover(){
        Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            for(Player player : Bukkit.getOnlinePlayers()){
                if(player.getPersistentDataContainer().has(NEEDS_TEMP_SWORD_REMOVED, PersistentDataType.STRING)) {
                    removePotentialTempSword(player);
                }
            }
        }, 0L, 20L * 5);
    }

    private void removePotentialTempSword(Player player){
        PlayerInventory inv = player.getInventory();
        for(ItemStack stack : inv.getContents()){
            if(stack != null && stack.getItemMeta() != null){
                boolean isTempSword = stack.getItemMeta().getPersistentDataContainer().has(TEMP_SWORD_MARKER, PersistentDataType.STRING);
                if(isTempSword){
                    PersistentDataContainer pdc = player.getPersistentDataContainer();
                    if(pdc.has(NEEDS_TEMP_SWORD_REMOVED, PersistentDataType.STRING)){
                        pdc.remove(NEEDS_TEMP_SWORD_REMOVED);
                    }
                    inv.remove(stack);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        removePotentialTempSword(e.getPlayer());
    }

    @Override
    public void initDependableData() {
        startTemporarySwordRemover();
    }
}
