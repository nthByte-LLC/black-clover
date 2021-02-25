package net.dohaw.blackclover.listener;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.event.PlayerCastSpellEvent;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.Projectable;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.PlayerDataManager;
import net.dohaw.blackclover.util.PDCHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;

public class PlayerWatcher implements Listener {

    private BlackCloverPlugin plugin;

    public PlayerWatcher(BlackCloverPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        plugin.getPlayerDataManager().loadData(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        PlayerDataManager pdm = plugin.getPlayerDataManager();
        UUID uuid = e.getPlayer().getUniqueId();
        pdm.saveData(uuid);
        pdm.removeDataFromMemory(uuid);
    }

    @EventHandler
    public void onPrepareToCast(PlayerInteractEvent e){

        Action action = e.getAction();
        if(action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR){

            ItemStack item = e.getItem();
            PlayerData pd = plugin.getPlayerDataManager().getData(e.getPlayer().getUniqueId());
            Player player = pd.getPlayer();
            CastSpellWrapper spellBoundToItem = PDCHandler.getSpellBoundToItem(pd, item);

            if(spellBoundToItem != null){

                SpellType spellType = spellBoundToItem.getKEY();
                e.setCancelled(true);
                if(pd.isSpellActive(spellType)){
                    if(player.isSneaking()){
                        pd.removeActiveSpell(spellType);
                    }
                }

                if(!pd.isSpellOnCooldown(spellType)){
                    if(pd.hasSufficientManaForSpell(spellBoundToItem)){
                        boolean wasSuccessfullyCasted = spellBoundToItem.cast(e, pd);
                        Bukkit.getPluginManager().callEvent(new PlayerCastSpellEvent(pd, spellBoundToItem, wasSuccessfullyCasted));
                    }else{
                        player.sendMessage("You don't have enough mana at the moment!");
                    }
                }else{
                    player.sendMessage("This spell is on cooldown!");
                }

            }else{
                System.out.println("NOT SPELL BOUND");
            }
        }

    }

//    @EventHandler
//    public void onGrimmoireUse(PlayerInteractEvent e){
//        ItemStack stack = e.getItem();
//        if(PDCHandler.isGrimmoire(stack)){
//            if(e.getHand() == EquipmentSlot.OFF_HAND){
//                e.setCancelled(true);
//            }
//        }
//    }

    @EventHandler
    public void onDamagedByProjectile(EntityDamageByEntityEvent e){

        Entity eDamaged = e.getEntity();
        Entity eDamager = e.getDamager();

        if(eDamager instanceof Projectile){
            Projectile proj = (Projectile) eDamager;
            ProjectileSource projSource = proj.getShooter();
            if(projSource instanceof Player){

                Player damager = (Player) projSource;
                PlayerDataManager pdm = plugin.getPlayerDataManager();
                PlayerData pdDamager = pdm.getData(damager.getUniqueId());

                Projectable projectableSpellWrapper = (Projectable) PDCHandler.getSpellBoundToProjectile(pdDamager, proj);
                if(projectableSpellWrapper != null){
                    projectableSpellWrapper.onHit(e, eDamaged, pdDamager);
                }else{
                    System.out.println("NOT SPELL BOUND PROJECTILE");
                }

            }
        }


    }

    @EventHandler
    public void onPostCast(PlayerCastSpellEvent e){

        if(e.isWasSuccessfullyCasted()){
            PlayerData pd = e.getPlayerData();
            CastSpellWrapper spellCasted = e.getSpellCasted();
            pd.getSpellsOnCooldown().add(spellCasted.getKEY());
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                pd.getSpellsOnCooldown().remove(spellCasted.getKEY());
                Bukkit.broadcastMessage("NOT ON COOLDOWN ANYMORE");
            }, (long) (spellCasted.getCooldown() * 20));
        }

    }

    private ItemStack ensureProperHotbar(){

        return null;

    }

}
