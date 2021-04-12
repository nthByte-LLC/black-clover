package net.dohaw.blackclover.grimmoire.spell.type.gravity;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.GravityPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Float extends ActivatableSpellWrapper implements Listener {

    private double floatHeight;

    public Float(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FLOAT, grimmoireConfig);
    }

    @Override
    public void doRunnableTick(PlayerData caster) {

        if(caster instanceof GravityPlayerData){

            Player player = caster.getPlayer();
            player.setAllowFlight(true);
            player.setFlying(true);

            GravityPlayerData gpd = (GravityPlayerData) caster;
            gpd.setFloating(true);
            gpd.setFloatY(player.getLocation().getY() + floatHeight);

            SpellUtils.spawnParticle(player, Particle.END_ROD, 30, 1, 1, 1);
            SpellUtils.playSound(player, Sound.BLOCK_BEACON_ACTIVATE);
        }

    }

    @Override
    public void deactiveSpell(PlayerData caster) throws UnexpectedPlayerData {

        Player player = caster.getPlayer();
        if(caster instanceof GravityPlayerData){
            GravityPlayerData gpd = (GravityPlayerData) caster;
            gpd.setFloating(false);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, (int) (10 * 20L), 0));
        }else{
            throw new UnexpectedPlayerData();
        }

    }

    @Override
    public void prepareShutdown() { }

    /*
        Levitates the player without the levitate potion effect
     */
    @EventHandler
    public void onPlayerFloat(PlayerMoveEvent e) throws UnexpectedPlayerData {

        Player player = e.getPlayer();
        PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
        Location to = e.getTo();
        if(to != null){
            if(pd.getGrimmoireWrapper().getKEY() == GrimmoireType.GRAVITY){
                if(pd instanceof GravityPlayerData){
                    GravityPlayerData gpd = (GravityPlayerData) pd;
                    if(gpd.isFloating()){
                        Vector velocity = player.getVelocity();
                        velocity.setY(0);
                        player.setVelocity(velocity);
                        to.setY(gpd.getFloatY());
                        player.teleport(to);
                    }
                }else{
                    throw new UnexpectedPlayerData();
                }
            }
        }

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.floatHeight = grimmoireConfig.getDoubleSetting(KEY, "Float Height");
    }

}
