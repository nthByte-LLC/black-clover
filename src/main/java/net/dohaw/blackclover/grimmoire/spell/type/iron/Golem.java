package net.dohaw.blackclover.grimmoire.spell.type.iron;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DependableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Golem extends CastSpellWrapper implements DependableSpell, Listener {

    private double duration;
    private int castDistance;
    private HashMap<Entity, IronGolem> golems = new HashMap<>();

    public Golem(GrimmoireConfig grimmoireConfig) {
        super(SpellType.GOLEM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        Entity entityInSight = getEntityInLineOfSight(e, player, 999);
        if(isTargetValid(player, entityInSight)) {
            org.bukkit.entity.IronGolem golem = (IronGolem) player.getWorld().spawnEntity(player.getLocation(), EntityType.IRON_GOLEM);
            golem.setTarget((LivingEntity) entityInSight);
            golems.put(entityInSight, golem);
            Bukkit.getServer().getWorld(golem.getWorld().getName()).playEffect(golem.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
            Bukkit.getServer().getWorld(golem.getWorld().getName()).playSound(golem.getLocation(), Sound.ENTITY_HORSE_ARMOR, 10, 10);
        }
        return true;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity en = e.getEntity();
        if (golems.containsKey(en)) {
            IronGolem golem = golems.get(en);
            Bukkit.getServer().getWorld(golem.getWorld().getName()).playEffect(golem.getLocation(), Effect.SMOKE, 10);
            Bukkit.getServer().getWorld(golem.getWorld().getName()).playSound(golem.getLocation(), Sound.ENTITY_FOX_DEATH, 10, 10);
            golems.remove(en);
            golem.remove();
        }
    }

    @EventHandler
    public void onTargetChange(EntityTargetEvent e) {
        if (e.getEntity() instanceof IronGolem) {
            if (golems.values().contains(e.getEntity())) {
                e.setCancelled(true);
            }
        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

    @Override
    public void prepareShutdown() {}
    @Override
    public void initDependableData() {
    }
    public static Entity getEntityInLineOfSight(Event e, Player player, int maxDistance){

        if(e instanceof EntityDamageByEntityEvent){
            return ((EntityDamageByEntityEvent) e).getEntity();
        }else{

            Location start = player.getLocation();
            Vector dir = start.getDirection();
            for (double i = 0; i < maxDistance; i += 0.5) {
                Vector currentDir = dir.clone().multiply(i);
                Location currentLocation = start.clone().add(currentDir);
                List<Entity> nearbyEntities = new ArrayList<>(player.getWorld().getNearbyEntities(currentLocation, 1, 1, 1, (entity) -> entity instanceof LivingEntity));
                nearbyEntities.removeIf(entity -> entity.getUniqueId().equals(player.getUniqueId()));
                if(!nearbyEntities.isEmpty()){
                    return nearbyEntities.get(0);
                }
            }

        }

        return null;
    }

    public static boolean isTargetValid(Player player, Entity entityInSight){
        return isTargetValid(player, entityInSight, LivingEntity.class);
    }

    public static boolean isTargetValid(Player player, Entity entityInSight, Class<?> targetType){
        if(entityInSight != null){
            if(targetType.isInstance(entityInSight)){
                return true;
            }else{
                player.sendMessage("This is not a valid entity!");
                return false;
            }
        }else{
            player.sendMessage("There is not entity within a reasonable distance from you!");
            return false;
        }
    }
}
