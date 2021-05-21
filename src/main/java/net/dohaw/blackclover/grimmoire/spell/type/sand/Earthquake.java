package net.dohaw.blackclover.grimmoire.spell.type.sand;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.EarthquakeRunner;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Earthquake extends CastSpellWrapper {

    private double damage;
    private int numberOfWaves;
    private int radius;

    public Earthquake(GrimmoireConfig grimmoireConfig) {
        super(SpellType.EARTHQUAKE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        Location earthquakeOrigin = getEarthquakeOrigin(player);
        if(earthquakeOrigin == null){
            player.sendMessage("You aren't near the ground. You can't cast this spell!");
            return false;
        }
        new EarthquakeRunner(player, earthquakeOrigin, this).runTaskTimer(Grimmoire.instance, 0L, 5L);
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getIntegerSetting(KEY, "Radius");
        this.numberOfWaves = grimmoireConfig.getIntegerSetting(KEY, "Number of Waves");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
    }

    public int getRadius() {
        return radius;
    }

    public int getNumberOfWaves() {
        return numberOfWaves;
    }

    public double getDamage() {
        return damage;
    }

    /**
     * Gets the origin of the location. An earthquake can only be cast on the ground. If the player isn't near the ground (Within 5 blocks of the ground), then it won't be cast
     * @param player The player that is casting the spell
     * @return The nearest location under the player that is a solid. Will return null if the player isn't near the ground
     */
    private Location getEarthquakeOrigin(Player player){
        for (int i = 0; i <= 5; i++) {
            Location checkedLocation = player.getLocation().clone().subtract(0, i, 0);
            if(checkedLocation.getBlock().getType().isSolid()){
                return checkedLocation;
            }
        }
        return null;
    }

}
