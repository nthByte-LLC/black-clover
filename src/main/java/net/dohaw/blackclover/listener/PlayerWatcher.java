package net.dohaw.blackclover.listener;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.event.*;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.TimeCastable;
import net.dohaw.blackclover.grimmoire.spell.type.water.Drowned;
import net.dohaw.blackclover.grimmoire.spell.type.wind.Hurricane;
import net.dohaw.blackclover.menu.GrimmoireMenu;
import net.dohaw.blackclover.playerdata.CompassPlayerData;
import net.dohaw.blackclover.playerdata.FungusPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.PlayerDataManager;
import net.dohaw.blackclover.runnable.ProjectileWaterHitChecker;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.PDCHandler;
import net.dohaw.blackclover.util.ProgressSystem;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.StringUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerWatcher implements Listener {

    private BlackCloverPlugin plugin;

    public PlayerWatcher(BlackCloverPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();
        plugin.getPlayerDataManager().loadData(player);

        PlayerData pd = plugin.getPlayerDataManager().getData(player.getUniqueId());
        if(pd instanceof CompassPlayerData){
            CompassPlayerData cpd = (CompassPlayerData) pd;
            PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
            for(Location waypoint : cpd.getWaypoints().values()){
                //TODO: Make client side name tag for waypoints (Compass Grimmoire)
//                System.out.println("LOCATION: " + waypoint.toString());
//                EntityArmorStand stand = SpellUtils.nmsInvisibleArmorStand(waypoint);
//                conn.sendPacket(new PacketPlayOutSpawnEntity(stand));
//                conn.sendPacket(new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), false));
            }
        }

        // They'll be in for a rude awakening if they log out while they were in a hurricane with setAllowFlight set to true...
        boolean wasInHurricanePreviously = player.getPersistentDataContainer().has(Hurricane.NSK_MARKER, PersistentDataType.STRING);
        if(wasInHurricanePreviously){
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setGravity(true);
            player.getPersistentDataContainer().remove(Hurricane.NSK_MARKER);
        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        PlayerDataManager pdm = plugin.getPlayerDataManager();
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        pdm.saveData(uuid);
        pdm.removeDataFromMemory(uuid);
        player.setInvisible(false);
    }

    @EventHandler
    public void onPrepareToCast(PlayerInteractEvent e){

        Player player = e.getPlayer();
        if(hasGrimmoireInOffHand(player)){

            Action action = e.getAction();
            if(action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR){
                castPotentialSpell(e, e.getPlayer());
            }else if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){
                e.setCancelled(true);
                GrimmoireMenu menu = new GrimmoireMenu(plugin);
                menu.initializeItems(player);
                menu.openInventory(player);
            }

        }

    }

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

        if(eDamager instanceof Projectile && eDamaged instanceof LivingEntity){

            LivingEntity damaged = (LivingEntity) eDamaged;
            Projectile proj = (Projectile) eDamager;
            ProjectileSource projSource = proj.getShooter();
            if(projSource instanceof Player){

                Player damager = (Player) projSource;
                PlayerDataManager pdm = plugin.getPlayerDataManager();
                PlayerData pdDamager = pdm.getData(damager.getUniqueId());

                CastSpellWrapper castSpellWrapper = PDCHandler.getSpellBoundToProjectile(pdDamager, proj);
                if(castSpellWrapper != null){
                    double damageDone = SpellUtils.callSpellDamageEvent(castSpellWrapper.getKEY(), damaged, damager, e.getDamage());
                    if(damageDone != SpellUtils.DAMAGE_CANCEL_VALUE){
                        e.setDamage(damageDone);
                    }else{
                        e.setCancelled(true);
                    }
                }

            }

        }

    }

    @EventHandler
    public void onPostCast(PostCastSpellEvent e){

        if(e.isWasSuccessfullyCasted()){

            PlayerData pd = e.getPlayerData();

            ProgressSystem.increaseExperience(pd);

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

    /**
     * Need this runner to run for the Water Wall
     * @see net.dohaw.blackclover.grimmoire.spell.type.water.WaterWall
     */
    @EventHandler
    public void onProjLaunch(ProjectileLaunchEvent e){
        Projectile projectile = e.getEntity();
        new ProjectileWaterHitChecker(projectile).runTaskTimer(plugin, 0L, 2L);
    }

    @EventHandler
    public void onStartCastSpell(StartTimedCastSpellEvent e){
        Player player = e.getCaster();
        PlayerData pd = plugin.getPlayerDataManager().getData(player.getUniqueId());
        pd.setCurrentlyCasting(true);
        pd.setCastStartHealth(player.getHealth());
        pd.setSpellCurrentlyCasting(e.getSpell());
    }

    /*
        Removes the tasks that are associated with the spell on death.
        Also gives xp to the killer if they are a player
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){

        Player deadPlayer = e.getEntity();
        PlayerData pd = plugin.getPlayerDataManager().getData(deadPlayer.getUniqueId());
        Map<SpellType, List<BukkitTask>> spellTasks = pd.getSpellRunnables();
        for (SpellType spell : spellTasks.keySet()) {
            List<BukkitTask> tasks = spellTasks.remove(spell);
            tasks.forEach(BukkitTask::cancel);
        }
        // if they die while casting, then we just stop casting.
        if(pd.isCurrentlyCasting()){
            pd.stopTimedCast();
        }
        pd.setFrozen(false);
        pd.setCanCast(true);
        pd.setCanAttack(true);

        /*
            Increases a player's XP if the killer is a player
         */
        Player killer = deadPlayer.getKiller();
        // They weren't killed by a player if this is null
        if(killer != null){
            PlayerData killerPlayerData = plugin.getPlayerDataManager().getData(killer);
            ProgressSystem.increaseExperience(killerPlayerData);
        }

    }

    /*
        Gives XP to the player if they have killed an entity
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){

        LivingEntity deadEntity = e.getEntity();
        Player xpGainer = deadEntity.getKiller();
        PlayerData xpGainerData = null;
        if(xpGainer != null){
            xpGainerData = plugin.getPlayerDataManager().getData(xpGainer);
        }else{
            Player petKillerOwner = getPetKillerOwner(deadEntity);
            if(petKillerOwner != null){
                xpGainerData = plugin.getPlayerDataManager().getData(petKillerOwner);
            }
        }

        if(xpGainerData != null){
            ProgressSystem.increaseExperience(xpGainerData);
        }

    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onSpellDamageWhileCast(EntityDamageByEntityEvent e){

        Entity eDamaged = e.getEntity();
        if(eDamaged instanceof Player){

            Player damaged = (Player) eDamaged;
            PlayerData damagedPlayerData = Grimmoire.instance.getPlayerDataManager().getData(damaged.getUniqueId());
            // Doesn't allow player's to do sweeping damage.
            if(!damagedPlayerData.isCanAttack()){
                System.out.println("CANT ATTACK");
                e.setCancelled(true);
                return;
            }

            if(damagedPlayerData.isCurrentlyCasting()){

                final int CAST_HEALTH_DIFF = 10;
                double stopCastHealthTreshold = damagedPlayerData.getCastStartHealth() - CAST_HEALTH_DIFF;
                if(stopCastHealthTreshold < 0){
                    stopCastHealthTreshold = 0;
                }

                // when they lose 5 hearts from their initial health amount from when they started casting, then stop the casting.
                if(stopCastHealthTreshold >= damaged.getHealth()){
                    SpellType spell = damagedPlayerData.getSpellCurrentlyCasting();
                    Bukkit.getServer().getPluginManager().callEvent(new StopTimedCastSpellEvent(damaged, spell, StopTimedCastSpellEvent.Cause.CANCELED_BY_DAMAGE));
                }

            }

        }
    }

    @EventHandler
    public void onStopTimedCastSpell(StopTimedCastSpellEvent e){
        StopTimedCastSpellEvent.Cause cause = e.getCause();
        Player caster = e.getCaster();
        SpellType spell = e.getSpell();
        PlayerData casterData = plugin.getPlayerDataManager().getData(caster.getUniqueId());
        casterData.stopTimedCast();
        if(cause == StopTimedCastSpellEvent.Cause.CANCELED_BY_DAMAGE){
            casterData.getSpellsOnCooldown().remove(spell);
        }
    }

    // Freezes players
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){

        Location from = e.getFrom();
        Location to = e.getTo();
        Player player = e.getPlayer();
        PlayerData pd = plugin.getPlayerDataManager().getData(player.getUniqueId());
        if(pd.isFrozen()){
            if(LocationUtil.hasMoved(to, from, true)){
                e.setCancelled(true);
            }
        }

    }

    // makes player invulnerable
    @EventHandler
    public void onPlayerTakeDamager(EntityDamageEvent e){
        Entity damagedEntity = e.getEntity();
        if(damagedEntity instanceof Player){
            Player damagedPlayer = (Player) damagedEntity;
            PlayerData pd = plugin.getPlayerDataManager().getData(damagedPlayer.getUniqueId());
            if(pd.isInVulnerable()){
                System.out.println("INVULV");
                SpellUtils.playSound(damagedPlayer, Sound.ITEM_SHIELD_BLOCK);
                SpellUtils.spawnParticle(damagedEntity, Particle.VILLAGER_ANGRY, 10, 0.3f, 0.3f, 0.3f);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSwitchHotbarSlot(PlayerItemHeldEvent e){

        Player player = e.getPlayer();
        if(hasGrimmoireInOffHand(player)){

            int slot = e.getNewSlot();

            PlayerData playerData = plugin.getPlayerDataManager().getData(player.getUniqueId());
            CastSpellWrapper spellBoundToSlot = Grimmoire.getSpellBoundToSlot(playerData, slot);
            if(spellBoundToSlot != null && playerData.isSpellUnlocked(spellBoundToSlot)){
                String properName = spellBoundToSlot.getKEY().toProperName();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(StringUtils.colorString("&f&l" + properName)));
            }

        }

    }

    /*
        When you punch a player, the PlayerInteractEvent does not run and the spell will not get cast. This casts a potential spell if they punch an entity
     */
    @EventHandler
    public void onPunchEntity(EntityDamageByEntityEvent e){

        Entity eDamager = e.getDamager();
        if(eDamager instanceof Player){
            Player player = (Player) eDamager;
            if(hasGrimmoireInOffHand(player)){
                castPotentialSpell(e, player);
            }
        }

    }

    private boolean hasGrimmoireInOffHand(Player player){
        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        return PDCHandler.isGrimmoire(offHandItem);
    }

    private void castPotentialSpell(Cancellable e, Player player){

        PlayerData pd = plugin.getPlayerDataManager().getData(player.getUniqueId());
        CastSpellWrapper spellBoundToSlot = Grimmoire.getSpellBoundToSlot(pd, player.getInventory().getHeldItemSlot());

        if(spellBoundToSlot != null && pd.isSpellUnlocked(spellBoundToSlot)){

            SpellType spellType = spellBoundToSlot.getKEY();
            if(canCast(pd, spellType) && !pd.isCurrentlyCasting()){

                e.setCancelled(true);
                // Deactivate all spell effects and runnables
                if(pd.isSpellActive(spellType) && player.isSneaking()){

                    ActivatableSpellWrapper apw = (ActivatableSpellWrapper) spellBoundToSlot;
                    try {
                        apw.deactiveSpell(pd);
                    } catch (UnexpectedPlayerData unexpectedPlayerData) {
                        unexpectedPlayerData.printStackTrace();
                    }

                    pd.stopSpellRunnables(spellType);

                    // This event is called just in case we want to do anything to the player after we remove the active spell
                    PostStopActiveSpellEvent stopActiveSpellEvent = new PostStopActiveSpellEvent(spellType, player, player, PostStopActiveSpellEvent.Cause.SELF_STOP);
                    Bukkit.getPluginManager().callEvent(stopActiveSpellEvent);
                    return;

                }

                if(!pd.isSpellOnCooldown(spellType)){
                    if(!pd.isSpellActive(spellType)){
                        if(pd.hasSufficientRegenForSpell(spellBoundToSlot)){

                            // This event is called just in case you want to do anything before you start the activatable spell runnables (We do that in the Water Control spell)
                            if(spellBoundToSlot instanceof ActivatableSpellWrapper){
                                PreStartActiveSpellEvent preStartActiveSpellEvent = new PreStartActiveSpellEvent(spellType, player);
                                Bukkit.getPluginManager().callEvent(preStartActiveSpellEvent);
                            }

                            if(spellBoundToSlot instanceof TimeCastable){
                                StartTimedCastSpellEvent event = new StartTimedCastSpellEvent(player, spellType);
                                Bukkit.getPluginManager().callEvent(event);
                            }

                            boolean wasSuccessfullyCasted;
                            try{
                                wasSuccessfullyCasted = spellBoundToSlot.cast((Event) e, pd);
                            } catch (UnexpectedPlayerData unexpectedPlayerData) {
                                unexpectedPlayerData.printStackTrace();
                                wasSuccessfullyCasted = false;
                            }

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

            }else{
                if (!pd.canCast()){
                    player.sendMessage("You can't cast right now!");
                }else if(pd.isCurrentlyCasting()){
                    player.sendMessage("You are currently casting a spell!");
                }
            }

        }else{
            System.out.println("NOT SPELL BOUND");
        }

    }

    private boolean canCast(PlayerData pd, SpellType spell){
        GrimmoireType grimmoireType = pd.getGrimmoireWrapper().getKEY();
        if(grimmoireType == GrimmoireType.FUNGUS){
            FungusPlayerData fpd = (FungusPlayerData) pd;
            // allows a player to cast while they're morphed, but only the morph spell
            if(fpd.isMorphed() && spell == SpellType.MORPH){
                return true;
            }
        }
        return pd.canCast();
    }

    /**
     * If the dead entity has died to a pet, this method will return the player that owns this pet.
     */
    private Player getPetKillerOwner(LivingEntity deadEntity){

        /*
            Rewards the player if their pet/tamed animal kills an entity
         */
        EntityDamageEvent lastDamageEvent = deadEntity.getLastDamageCause();
        if(lastDamageEvent != null){

            if(lastDamageEvent instanceof EntityDamageByEntityEvent){

                EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) lastDamageEvent;
                Entity damager = event.getDamager();
                if(damager instanceof Tameable){

                    AnimalTamer petOwner = ((Tameable) damager).getOwner();
                    if(petOwner instanceof Player){
                        return (Player) petOwner;
                    }


                }else if(damager.getType() == EntityType.DROWNED){
                    // Player's can spawn Drowns from the Water Grimmoire (The "Drowned" spell)
                    PersistentDataContainer pdc = damager.getPersistentDataContainer();
                    if(pdc.has(Drowned.NSK_MARK, PersistentDataType.STRING)){
                        String drownedOwnerName = pdc.get(Drowned.NSK_MARK, PersistentDataType.STRING);
                        return Bukkit.getPlayer(drownedOwnerName);
                    }

                }

            }

        }

        return null;

    }

}
