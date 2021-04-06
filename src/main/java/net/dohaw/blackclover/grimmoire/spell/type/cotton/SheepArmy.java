package net.dohaw.blackclover.grimmoire.spell.type.cotton;

import lombok.Getter;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.CottonPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.SheepArmyGoalChecker;
import net.dohaw.blackclover.util.AttributeHelper;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class SheepArmy extends CastSpellWrapper {


    @Getter
    private double explosionDistance;
    private int castDistance, numSheep;

    private double movementSpeedAdditive;

    @Getter
    private int damage;

    public SheepArmy(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SHEEP_ARMY, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        if(pd instanceof CottonPlayerData){

            Player player = pd.getPlayer();
            CottonPlayerData cpd = (CottonPlayerData) pd;
            if(!cpd.isArmySpawned()){

                Entity entityInSight = SpellUtils.getEntityInLineOfSight(player, castDistance);
                if(SpellUtils.isTargetValid(player, entityInSight)){

                    Location sheepSpawn = LocationUtil.getLocationInFront(player, 1).add(0, 1, 0);
                    World world = player.getWorld();

                    SpellUtils.playSound(sheepSpawn, Sound.ITEM_CHORUS_FRUIT_TELEPORT);
                    SpellUtils.spawnParticle(sheepSpawn, Particle.END_ROD, 10, 1, 1, 1);
                    List<Sheep> army = new ArrayList<>();
                    for(int i = 0; i < numSheep; i++){
                        org.bukkit.entity.Sheep sheep = (org.bukkit.entity.Sheep) world.spawnEntity(sheepSpawn, EntityType.SHEEP);
                        sheep.setTarget((LivingEntity)entityInSight);
                        army.add(sheep);
                        AttributeHelper.alterAttribute(sheep, Attribute.GENERIC_MOVEMENT_SPEED, movementSpeedAdditive);
                    }
                    new SheepArmyGoalChecker(army, cpd, (LivingEntity) entityInSight, this);

                    cpd.setArmy(army);
                    return true;

                }

            }else{
                if(player.isSneaking()){
                    cpd.removeArmy();
                }else{
                    player.sendMessage("The sheep army has already been called!");
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
        this.numSheep = grimmoireConfig.getIntegerSetting(KEY, "Number of Sheep");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.explosionDistance = grimmoireConfig.getDoubleSetting(KEY, "Explosion Distance");
        this.damage = grimmoireConfig.getIntegerSetting(KEY, "Damage");
        this.movementSpeedAdditive = grimmoireConfig.getDoubleSetting(KEY, "Movement Speed Additive");
    }
}
