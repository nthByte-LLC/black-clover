package net.dohaw.blackclover.grimmoire.spell.type.gravity;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Stuns a player
 */
public class Gravity extends CastSpellWrapper {

    private double duration;
    private int castDistance;

    public Gravity(GrimmoireConfig grimmoireConfig) {
        super(SpellType.GRAVITY, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, entityInSight)){
            if(entityInSight instanceof Player){

                Player target = (Player) entityInSight;
                target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (duration * 20), 9));

                PlayerData targetPlayerData = Grimmoire.instance.getPlayerDataManager().getData(target.getUniqueId());
                targetPlayerData.setFrozen(true);
                SpellUtils.playSound(entityInSight, Sound.BLOCK_ANVIL_PLACE);
                SpellUtils.spawnParticle(entityInSight, Particle.VILLAGER_ANGRY, 30, 1, 1, 1);

                return true;

            }else{
                player.sendMessage("This is not a valid player to target!");
            }
        }
        return false;

    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }
}
