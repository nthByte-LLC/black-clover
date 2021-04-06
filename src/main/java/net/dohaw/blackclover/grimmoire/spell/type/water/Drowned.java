package net.dohaw.blackclover.grimmoire.spell.type.water;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.WaterPlayerData;
import net.dohaw.blackclover.util.AttributeHelper;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.ResponderFactory;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class Drowned extends CastSpellWrapper implements Listener {

    // pdc value contains who spawned the drowned.
    private final NamespacedKey NSK_MARK = NamespacedKey.minecraft("summoned-drowned");

    private double movementSpeedAdditive;
    private int absorptionAmount;
    private int castDistance;

    public Drowned(GrimmoireConfig grimmoireConfig) {
        super(SpellType.DROWNED, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        if(pd instanceof WaterPlayerData){

            WaterPlayerData wpd = (WaterPlayerData) pd;
            Player player = pd.getPlayer();
            ResponderFactory rf = new ResponderFactory(player);

            if(!wpd.isDrownedSummoned()){

                Entity entityInSight = SpellUtils.getEntityInLineOfSight(player, castDistance);

                if(entityInSight != null){

                    if(entityInSight instanceof LivingEntity){

                        LivingEntity leInSight = (LivingEntity) entityInSight;
                        Location blockInFront = LocationUtil.getLocationInFront(player, 1);
                        org.bukkit.entity.Drowned drowned = (org.bukkit.entity.Drowned) player.getWorld().spawnEntity(blockInFront.add(0, 1, 0), EntityType.DROWNED);
                        drowned.getPersistentDataContainer().set(NSK_MARK, PersistentDataType.STRING, player.getName());
                        drowned.setAbsorptionAmount(absorptionAmount);
                        drowned.setTarget(leInSight);

                        AttributeHelper.alterAttribute(drowned, Attribute.GENERIC_MOVEMENT_SPEED, movementSpeedAdditive);

                        SpellUtils.spawnParticle(drowned, Particle.WATER_BUBBLE, 30, 1, 1, 1);
                        SpellUtils.playSound(drowned, Sound.ENTITY_DROWNED_AMBIENT_WATER);

                        wpd.setDrowned(drowned);
                        return true;

                    }else{
                        rf.sendMessage("This is not a valid entity!");
                    }

                }else{
                    rf.sendMessage("There is no entity within reasonable distance of you!");
                }

            }else{
                if(player.isSneaking()){
                    wpd.removeDrowned();
                }else{
                    rf.sendMessage("You already have a drowned spawned!");
                }
            }

        }else{
            throw new UnexpectedPlayerData();
        }

        return false;
    }


    @Override
    public void loadSettings() {
        super.loadSettings();
        this.movementSpeedAdditive = grimmoireConfig.getDoubleSetting(KEY, "Movement Speed Additive");
        this.absorptionAmount = grimmoireConfig.getIntegerSetting(KEY, "Absorption Amount");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

    /*
        Prevents drowns from burning in the sunlight.
     */
    @EventHandler
    public void onDrownedBurn(EntityCombustEvent e){
        Entity entity = e.getEntity();
        if(entity.getPersistentDataContainer().has(NSK_MARK, PersistentDataType.STRING)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTargetSummoner(EntityTargetLivingEntityEvent e){
        Entity entity = e.getEntity();
        if(entity.getPersistentDataContainer().has(NSK_MARK, PersistentDataType.STRING)){
            LivingEntity target = e.getTarget();
            if(target instanceof Player){
                String summonerName = entity.getPersistentDataContainer().get(NSK_MARK, PersistentDataType.STRING);
                // if they are trying to target the person that summoned them, then it cancels it.
                if(target.getName().equalsIgnoreCase(summonerName)){
                    e.setCancelled(true);
                }
            }

        }

    }

}
