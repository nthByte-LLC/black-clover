package net.dohaw.blackclover.grimmoire.spell.type.wind;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows a player to fly. You can adjust your flight speed by toggling between hotbar numbers 0-3
 */
public class Flight extends ActivatableSpellWrapper {

    private Map<Integer, Float> flightSpeeds;

    public Flight(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FLIGHT, grimmoireConfig);
    }

    @Override
    protected void activateRunnable(PlayerData pd) {

        Player player = pd.getPlayer();
        player.setAllowFlight(true);

        // Internal clock for setting flight speed.
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            int currentSlot = player.getInventory().getHeldItemSlot();
            if(currentSlot <= 3){
                player.setFlySpeed(flightSpeeds.get(currentSlot));
            }
        }, 0L, 5L);
        pd.addSpellRunnables(KEY, task);

        super.activateRunnable(pd);
    }

    @Override
    public void doRunnableTick(PlayerData caster) { }

    @Override
    public void deactiveSpell(PlayerData caster){
        Player player = caster.getPlayer();
        player.setAllowFlight(false);
        // default
        player.setFlySpeed(.1f);
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.flightSpeeds = new HashMap<>();
        for (int i = 0; i <= 3; i++) {
            flightSpeeds.put(i, (float) grimmoireConfig.getDoubleSetting(KEY, "Flight Speed Levels." + i));
        }
    }

}
