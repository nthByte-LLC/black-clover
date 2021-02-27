package net.dohaw.blackclover.grimmoire.spell.type.sand;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DamageableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.RayTraceResult;

public class SandGrave extends CastSpellWrapper implements DamageableSpell {

    private int castDistance;
    private int radiusBall;
    private int blocksAbovePlayer;

    public SandGrave(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SAND_GRAVE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Location playerLocation = player.getLocation();
        System.out.println("CAST DISTANCE: " + castDistance);
        RayTraceResult rtr = player.getWorld().rayTraceBlocks(playerLocation, playerLocation.getDirection(), castDistance);
        if(rtr != null){
            System.out.println("HERE");
            Entity entityHit = rtr.getHitEntity();
            if(entityHit != null){
                System.out.println("HERE");
                Location centerBall = entityHit.getLocation().add(0, blocksAbovePlayer + radiusBall, 0);
                spawnSandBall(player, centerBall, 1);
            }
        }
        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getNumberSetting(KEY, "Cast Distance");
        this.blocksAbovePlayer = grimmoireConfig.getNumberSetting(KEY, "Blocks Above Player");
        this.radiusBall = grimmoireConfig.getNumberSetting(KEY, "Radius Ball");
    }

    public void spawnSandBall(Player p, Location center, double density) {
        for (double x = -radiusBall; x < radiusBall; x += density) {
            for (double z = -radiusBall; z < radiusBall; z += density) {
                double y = Math.sqrt(radiusBall*radiusBall - x*x - z*z);
                Location loc1 = center.clone().subtract(-x, y, -z);
                Location loc2 = center.clone().add(-x, y, -z);
                p.getWorld().getBlockAt(loc1).setType(Material.SAND);
                p.getWorld().getBlockAt(loc2).setType(Material.SAND);
            }
        }
    }

}
