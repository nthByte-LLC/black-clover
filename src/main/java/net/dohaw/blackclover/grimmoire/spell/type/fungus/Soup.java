package net.dohaw.blackclover.grimmoire.spell.type.fungus;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Feeds either yourself or a target player the same amount that mushroom soup would do
 */
public class Soup extends CastSpellWrapper {

    private int feedAmount;
    private int castDistance;

    public Soup(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SOUP, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();

        if(player.isSneaking()){
            Entity entityInTarget = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
            if(SpellUtils.isTargetValid(player, entityInTarget, Player.class)){
                Player target = (Player) entityInTarget;
                feed(target);
                return true;
            }
        }else{
            feed(player);
            return true;
        }

        return false;
    }

    private void feed(Player player){

        int foodLevel = player.getFoodLevel();
        int newFoodLevel = foodLevel + feedAmount;
        if(foodLevel > 20){
            newFoodLevel = 20;
        }

        player.setFoodLevel(newFoodLevel);
        SpellUtils.spawnParticle(player, Particle.VILLAGER_HAPPY, 30, 1, 1, 1);
        SpellUtils.playSound(player, Sound.ENTITY_GENERIC_EAT);

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.feedAmount = grimmoireConfig.getIntegerSetting(KEY, "Feed Amount");
    }

}
