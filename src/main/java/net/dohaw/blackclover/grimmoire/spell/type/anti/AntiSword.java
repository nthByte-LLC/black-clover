package net.dohaw.blackclover.grimmoire.spell.type.anti;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;

/**
 * Allows you to absorb damage with your anti sword
 */
public class AntiSword extends CastSpellWrapper implements Listener {

    private final NamespacedKey NSK_TIME_MARK = NamespacedKey.minecraft("antisword-time-mark");
    private final NamespacedKey NSK_USES_MARK = NamespacedKey.minecraft("antisword-uses-mark");
    private int timeUsage, uses;

    public AntiSword(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ANTI_SWORD, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        ItemStack antiMagicSword = Grimmoire.ANTI.getAntiSword();
        Player player = pd.getPlayer();
        PlayerInventory playerInv = player.getInventory();
        if(playerInv.contains(antiMagicSword)){
            playerInv.remove(antiMagicSword);
        }

        LocalDateTime timeGiven = LocalDateTime.now();
        ItemMeta swordMeta = antiMagicSword.getItemMeta();
        PersistentDataContainer pdc = swordMeta.getPersistentDataContainer();
        pdc.set(NSK_TIME_MARK, PersistentDataType.STRING, timeGiven.toString());
        pdc.set(NSK_USES_MARK, PersistentDataType.INTEGER, 0);
        antiMagicSword.setItemMeta(swordMeta);

        playerInv.addItem(antiMagicSword);
        SpellUtils.playSound(player, Sound.ENTITY_PHANTOM_SWOOP);
        SpellUtils.spawnParticle(player, Particle.PORTAL, 10, 1, 1, 1);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            if(playerInv.contains(antiMagicSword)){
                SpellUtils.spawnParticle(player, Particle.SQUID_INK, 10, 1, 1, 1);
                SpellUtils.playSound(player, Sound.BLOCK_CHAIN_BREAK);
                playerInv.remove(antiMagicSword);
            }
        }, timeUsage * 20);

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.timeUsage = grimmoireConfig.getIntegerSetting(KEY, "Usage Time");
        this.uses = grimmoireConfig.getIntegerSetting(KEY, "Number of Uses");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTakeSpellDamage(SpellDamageEvent e){

        Entity eDamaged = e.getDamaged();
        if(eDamaged instanceof Player){
            Player damaged = (Player) eDamaged;
            PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(damaged.getUniqueId());
            if(pd.getGrimmoireWrapper().getKEY() == GrimmoireType.ANTI){

                ItemStack mainHand = damaged.getInventory().getItemInMainHand();
                ItemMeta mainHandMeta = mainHand.getItemMeta();
                if(mainHandMeta != null){

                    PersistentDataContainer mainHandPDC = mainHand.getItemMeta().getPersistentDataContainer();
                    // checks to see if it's a antisword sword
                    if(mainHandPDC.has(NSK_TIME_MARK, PersistentDataType.STRING)){

                        String dateAndTimeStr = mainHandPDC.get(NSK_TIME_MARK, PersistentDataType.STRING);
                        assert dateAndTimeStr != null;
                        LocalDateTime dateTime = LocalDateTime.parse(dateAndTimeStr);
                        LocalDateTime expirationTime = dateTime.plusSeconds(timeUsage);

                        Player player = pd.getPlayer();
                        // If the current time is after the expiration time, then they have tried to cheat-keep the sword
                        if(LocalDateTime.now().isAfter(expirationTime)){
                            player.getInventory().remove(mainHand);
                        }else{

                            // absorbs the damage
                            int currentNumUses = mainHandPDC.get(NSK_USES_MARK, PersistentDataType.INTEGER) + 1;
                            e.setCancelled(true);

                            if(currentNumUses == uses){
                                player.getInventory().remove(mainHand);
                                SpellUtils.spawnParticle(player, Particle.SQUID_INK, 10, 1, 1, 1);
                                SpellUtils.playSound(player, Sound.BLOCK_CHAIN_BREAK);
                            }else{
                                mainHandPDC.set(NSK_USES_MARK, PersistentDataType.INTEGER, currentNumUses);
                            }

                        }

                    }

                }

            }
        }

    }

}
