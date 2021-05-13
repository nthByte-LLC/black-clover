package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.spell.type.glass.Shield;
import net.dohaw.blackclover.util.SpellUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ShieldRunner extends BukkitRunnable {

    private List<Location> shieldBlockLocations = new ArrayList<>();

    private int shieldWidth;
    private int shieldHeight;
    private Player shieldUser;

    public ShieldRunner(Player shieldUser, Shield spell){
        this.shieldUser = shieldUser;
        this.shieldWidth = spell.getShieldWidth();
        this.shieldHeight = spell.getShieldHeight();
    }

    @Override
    public void run() {
        clearShieldBlocks();
        this.shieldBlockLocations = SpellUtils.makeWall(shieldUser.getLocation(), Material.GLASS, shieldWidth, shieldHeight, false);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        clearShieldBlocks();
        super.cancel();
    }

    private void clearShieldBlocks(){
        for (Location shieldBlockLocation : shieldBlockLocations) {
            shieldBlockLocation.getBlock().setType(Material.AIR);
        }
    }

}
