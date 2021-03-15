package net.dohaw.blackclover.listener;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.event.*;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.PlayerDataManager;
import net.dohaw.blackclover.util.PDCHandler;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.StringUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
        if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK){

            PlayerData pd = plugin.getPlayerDataManager().getData(e.getPlayer().getUniqueId());
            Player player = pd.getPlayer();
            CastSpellWrapper spellBoundToSlot = PDCHandler.getSpellBoundToSlot(pd, player.getInventory().getHeldItemSlot());

            if(spellBoundToSlot != null){

                if(pd.isCanCast()){

                    SpellType spellType = spellBoundToSlot.getKEY();
                    e.setCancelled(true);
                    if(pd.isSpellActive(spellType)){
                        if(player.isSneaking()){
                            pd.removeActiveSpell(spellType);
                            // This event is called just in case we want to do anything to the player after we remove the active spell
                            PostStopActiveSpellEvent stopActiveSpellEvent = new PostStopActiveSpellEvent(spellType, player, player, PostStopActiveSpellEvent.Cause.SELF_STOP);
                            Bukkit.getPluginManager().callEvent(stopActiveSpellEvent);
                            return;
                        }
                    }

                    if(!pd.isSpellOnCooldown(spellType)){
                        if(!pd.isSpellActive(spellType)){
                            if(pd.hasSufficientRegenForSpell(spellBoundToSlot)){
                                // This event is called just in case you want to do anything before you start the activatable spell runnables (We do that in the Water Control spell)
                                if(spellBoundToSlot instanceof ActivatableSpellWrapper){
                                    PreStartActiveSpellEvent preStartActiveSpellEvent = new PreStartActiveSpellEvent(spellType, player);
                                    Bukkit.getPluginManager().callEvent(preStartActiveSpellEvent);
                                }
                                boolean wasSuccessfullyCasted = spellBoundToSlot.cast(e, pd);
                                if(wasSuccessfullyCasted && !(spellBoundToSlot instanceof ActivatableSpellWrapper)){
                                    spellBoundToSlot.deductMana(pd);
                                }
                                Bukkit.getPluginManager().callEvent(new PostCastSpellEvent(pd, spellBoundToSlot, wasSuccessfullyCasted));
                            }else{
                                player.sendMessage("You don't have enough mana at the moment!");
                            }
                        }
                    }else{
                        player.sendMessage("This spell is on cooldown!");
                    }

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
    public void onCancelSpellDamage(SpellDamageEvent e){
        if(e.isCancelled()){
            Entity damaged = e.getDamaged();
            SpellUtils.playSound(damaged, Sound.ITEM_SHIELD_BLOCK);
            SpellUtils.spawnParticle(damaged, Particle.BLOCK_CRACK, Material.COAL_BLOCK.createBlockData(), 10, 1, 1, 1);
            System.out.println("SPELL HAS BEEN NEGATED");
        }
    }

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

                CastSpellWrapper castSpellWrapper = PDCHandler.getSpellBoundToProjectile(pdDamager, proj);
                if(castSpellWrapper != null){
                    SpellDamageEvent spellDamageEvent = new SpellDamageEvent(castSpellWrapper.getKEY(), e.getDamage(), eDamaged, damager);
                    Bukkit.getPluginManager().callEvent(spellDamageEvent);
                    if(spellDamageEvent.isCancelled()){
                        e.setCancelled(true);
                    }else{
                        System.out.println("DAMAGE PROJ: " + spellDamageEvent.getDamage());
                        e.setDamage(spellDamageEvent.getDamage());
                    }
                }else{
                    System.out.println("NOT SPELL BOUND PROJECTILE");
                }

            }
        }


    }

    @EventHandler
    public void onPostCast(PostCastSpellEvent e){

        if(e.isWasSuccessfullyCasted()){

            PlayerData pd = e.getPlayerData();
            Player player = pd.getPlayer();
            CastSpellWrapper spellCasted = e.getSpellCasted();

            String spellName = spellCasted.getKEY().toProperName();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(StringUtils.colorString("&7" + spellName + " casted! - " + (int) spellCasted.getCooldown() + "s")));

            pd.getSpellsOnCooldown().add(spellCasted.getKEY());
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                SpellType spellType = spellCasted.getKEY();
                pd.getSpellsOnCooldown().remove(spellType);
                Bukkit.getServer().getPluginManager().callEvent(new SpellOffCooldownEvent(spellType, player));
            }, (long) (spellCasted.getCooldown() * 20));
        }

    }

}
