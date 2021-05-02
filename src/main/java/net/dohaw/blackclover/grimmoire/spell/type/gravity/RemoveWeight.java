package net.dohaw.blackclover.grimmoire.spell.type.gravity;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.GravityPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Speeds up the player, but also applies weakness
 */
public class RemoveWeight extends CastSpellWrapper {

    private int levelSpeed, levelWeakness;
    private double duration;

    public RemoveWeight(GrimmoireConfig grimmoireConfig) {
        super(SpellType.REMOVE_WEIGHT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        if(pd instanceof GravityPlayerData) {

            Player player = pd.getPlayer();
            GravityPlayerData gpd = (GravityPlayerData) pd;
            // Can't cast add weight and remove weight at the same time.
            if (!gpd.isAddingWeight()) {

                gpd.setRemovingWeight(true);

                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (duration * 20), levelSpeed));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (int) (duration * 20), levelWeakness));
                SpellUtils.spawnParticle(player.getLocation(), Particle.REDSTONE, new Particle.DustOptions(Color.PURPLE, 1), 30, 1, 1, 1);
                SpellUtils.playSound(player, Sound.BLOCK_PUMPKIN_CARVE);

                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                    gpd.setRemovingWeight(false);
                }, (long) (duration * 20));

                return true;

            }else{
                player.sendMessage("You are already using the Add Weight ability!");
            }

        }

        return false;
    }

    @Override
    public void prepareShutdown() { }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
        this.levelWeakness = grimmoireConfig.getIntegerSetting(KEY, "Weakness Level") - 1;
        this.levelSpeed = grimmoireConfig.getIntegerSetting(KEY, "Speed Level") - 1;
    }

}
