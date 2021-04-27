package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.spell.type.lightning.Thunderstorm;
import net.dohaw.blackclover.grimmoire.type.Lightning;
import net.dohaw.blackclover.runnable.particle.EntityRunner;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.StringUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.ThreadLocalRandom;

public class ThunderstormRunner extends EntityRunner {

    private ThreadLocalRandom current;
    private Player caster;
    private int radius, numLightningStrikes;

    private Location lightningLocation;

    private Thunderstorm spell;

    private int count;

    public ThunderstormRunner(Player caster, Thunderstorm spell){
        super(caster);
        this.spell = spell;
        this.caster = caster;
        this.radius = spell.getRadius();
        this.numLightningStrikes = spell.getNumLightningStrikes();
        this.current = ThreadLocalRandom.current();
        this.lightningLocation = caster.getLocation();
    }

    @Override
    public void run() {

        if(areEntitiesValid()){

            if(count != numLightningStrikes){

                Location casterLocation = caster.getLocation();
                double maxX = casterLocation.getX() + radius;
                double minX = casterLocation.getX() - radius;
                double maxZ = casterLocation.getZ() + radius;
                double minZ = casterLocation.getZ() - radius;

                double randomX = current.nextDouble(minX, maxX);
                double randomZ = current.nextDouble(minZ, maxZ);

                World world = casterLocation.getWorld();
                assert world != null;

                double y = world.getHighestBlockYAt((int)randomX, (int)randomZ);

                lightningLocation.setY(y);
                lightningLocation.setX(randomX);
                lightningLocation.setZ(randomZ);
                LightningStrike strike =  world.strikeLightning(lightningLocation);
                markLightningStrike(strike);
                count++;

            }else{
                cancel();
            }

        }

    }

    private void markLightningStrike(LightningStrike strike){
        strike.getPersistentDataContainer().set(spell.STRIKE_OWNER_KEY, PersistentDataType.STRING, caster.getName());
    }

}
