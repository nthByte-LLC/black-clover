package net.dohaw.blackclover.grimmoire.spell.type.gravity;

import lombok.Getter;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Allows the player to hover at a certain y level.
 */
public class Float extends ActivatableSpellWrapper implements Listener {

    @Getter
    private double floatHeight;

    public Float(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FLOAT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        initFloat(pd);
        return super.cast(e, pd);
    }

    @Override
    public void doRunnableTick(PlayerData caster) { }

    @Override
    public void deactiveSpell(PlayerData caster) {

        Player player = caster.getPlayer();
        caster.setFloating(false);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, (int) (10 * 20L), 0));

    }

    public void initFloat(PlayerData playerData){

        Player player = playerData.getPlayer();
        player.setAllowFlight(true);
        player.setFlying(true);
        playerData.setFloating(true);
        playerData.setFloatY(player.getLocation().getY() + floatHeight);

        SpellUtils.spawnParticle(player, Particle.END_ROD, 30, 1, 1, 1);
        SpellUtils.playSound(player, Sound.BLOCK_BEACON_ACTIVATE);

    }

    @Override
    public void prepareShutdown() { }

    /*
        Levitates the player without the levitate potion effect
     */
    @EventHandler
    public void onPlayerFloat(PlayerMoveEvent e) {

        Player player = e.getPlayer();
        PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
        Location to = e.getTo();
        if(to != null){
            if(pd.isFloating()){
                to.setY(pd.getFloatY());
                player.teleport(to);
            }
        }

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.floatHeight = grimmoireConfig.getDoubleSetting(KEY, "Float Height");
    }

}
