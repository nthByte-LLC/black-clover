package net.dohaw.blackclover.grimmoire.spell.type.gravity;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.GravityPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Slows down the player, but also gives strength
 */
public class AddWeight extends CastSpellWrapper {

    private int levelSlowness, levelStrength;
    private double duration;

    public AddWeight(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ADD_WEIGHT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        if(pd instanceof GravityPlayerData){

            Player player = pd.getPlayer();
            GravityPlayerData gpd = (GravityPlayerData) pd;
            if(!gpd.isRemovingWeight()){

                gpd.setAddingWeight(true);

                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (duration * 20), levelSlowness - 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (duration * 20), levelStrength - 1));
                SpellUtils.playSound(player, Sound.ENTITY_VILLAGER_NO);
                SpellUtils.spawnParticle(player, Particle.VILLAGER_ANGRY, 30, 1, 1, 1);

                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                   gpd.setAddingWeight(false);
                }, (long) (duration * 20));

                return true;

            }else{
                player.sendMessage("You are already using the Remove Weight ability!");
            }

        }else{
            throw new UnexpectedPlayerData();
        }

        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.levelSlowness = grimmoireConfig.getIntegerSetting(KEY, "Slowness Level");
        this.levelStrength = grimmoireConfig.getIntegerSetting(KEY, "Strength Level");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

}
