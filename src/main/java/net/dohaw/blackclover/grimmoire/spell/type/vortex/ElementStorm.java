package net.dohaw.blackclover.grimmoire.spell.type.vortex;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.type.Vortex;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.vortex.*;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ElementStorm extends VortexSpell {

    public ElementStorm(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ELEMENT_STORM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        List<SpellType> elementSpells = new ArrayList<>(Arrays.asList(SpellType.FIRADO, SpellType.WATERHOSE, SpellType.EARTHSTORM, SpellType.TORNADO));
        ThreadLocalRandom current = ThreadLocalRandom.current();

        Player caster = pd.getPlayer();
        Location playerLocation = caster.getLocation();
        for(int yaw = 0; yaw <= 270; yaw += 90){

            Location turnedLocation = playerLocation.clone();
            turnedLocation.setYaw(yaw);
            Location locInFront = LocationUtil.getLocationInFront(turnedLocation, 1);
            SpellType randomElementSpell = elementSpells.remove(current.nextInt(elementSpells.size()));

            Entity invisibleArmorStand = SpellUtils.invisibleArmorStand(locInFront);
            Vortex vortex = Grimmoire.VORTEX;
            AbstractVortexTornadoRunner tornadoRunner;
            switch(randomElementSpell){
                case WATERHOSE:
                    tornadoRunner = new WaterhoseRunner(invisibleArmorStand, vortex.waterhose);
                    break;
                case EARTHSTORM:
                    tornadoRunner = new EarthstormRunner(invisibleArmorStand, vortex.earthstorm, caster);
                    break;
                case TORNADO:
                    tornadoRunner = new VortexTornadoRunner(invisibleArmorStand, vortex.tornado, caster);
                    break;
                case FIRADO:
                    tornadoRunner = new FiradoRunner(invisibleArmorStand, vortex.firado);
                    break;
                default:
                    tornadoRunner = null;
            }

            if(tornadoRunner != null){
                tornadoRunner.runTaskTimer(Grimmoire.instance, 0L, 5L);
            }

        }
        SpellUtils.playSound(caster, Sound.BLOCK_BEACON_POWER_SELECT);

        return true;
    }

    @Override
    public void prepareShutdown() {

    }

}
