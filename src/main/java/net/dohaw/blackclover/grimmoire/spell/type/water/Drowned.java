package net.dohaw.blackclover.grimmoire.spell.type.water;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.WaterPlayerData;
import net.dohaw.blackclover.util.BlockUtil;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.ResponderFactory;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Drowned extends CastSpellWrapper {

    private double movementSpeedAdditive;
    private int absorptionAmount;
    private int castDistance;

    public Drowned(GrimmoireConfig grimmoireConfig) {
        super(SpellType.DROWNED, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        if(pd instanceof WaterPlayerData){

            WaterPlayerData wpd = (WaterPlayerData) pd;
            Player player = pd.getPlayer();
            ResponderFactory rf = new ResponderFactory(player);
            Entity entityInSight = SpellUtils.getEntityInLineOfSight(player, castDistance);

            if(entityInSight != null){

                if(entityInSight instanceof LivingEntity){

                    LivingEntity leInSight = (LivingEntity) entityInSight;
                    if(!wpd.isDrownedSummoned()){

                        Location blockInFront = BlockUtil.getBlockInFront(player, 1);
                        org.bukkit.entity.Drowned drowned = (org.bukkit.entity.Drowned) player.getWorld().spawnEntity(blockInFront.add(0, 1, 0), EntityType.DROWNED);
                        drowned.setAbsorptionAmount(absorptionAmount);
                        drowned.setTarget(leInSight);

                        double movementSpeedBase = drowned.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
                        drowned.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(movementSpeedBase + movementSpeedAdditive);

                        SpellUtils.spawnParticle(drowned, Particle.WATER_BUBBLE, 30, 1, 1, 1);
                        SpellUtils.playSound(drowned, Sound.ENTITY_DROWNED_AMBIENT_WATER);

                        wpd.setDrowned(drowned);

                    }else{
                        if(player.isSneaking()){
                            wpd.removeDrowned();
                        }else{
                            rf.sendMessage("You already have a drowned spawned!");
                        }
                        return false;
                    }

                }else{
                    rf.sendMessage("This is not a valid entity!");
                    return false;
                }

            }else{
                rf.sendMessage("There is no entity within reasonable distance of you!");
                return false;
            }

        }else{
            try {
                throw new UnexpectedPlayerData();
            } catch (UnexpectedPlayerData unexpectedPlayerData) {
                unexpectedPlayerData.printStackTrace();
            }
        }

        return true;
    }


    @Override
    public void loadSettings() {
        super.loadSettings();
        this.movementSpeedAdditive = grimmoireConfig.getDoubleSetting(KEY, "Movement Speed Additive");
        this.absorptionAmount = grimmoireConfig.getIntegerSetting(KEY, "Absorption Amount");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

}
