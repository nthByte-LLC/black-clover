package net.dohaw.blackclover.runnable;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class InWaterChecker extends BukkitRunnable {

    private Player player;

    public InWaterChecker(Player player){
        this.player = player;
    }

    @Override
    public void run() {

        PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
        if(pd.getGrimmoireWrapper().getKEY() == GrimmoireType.WATER){
            if(pd.getActiveSpells().containsKey(SpellType.WATER_CONTROL)){
                boolean isInWater = player.getLocation().getBlock().isLiquid();
                if(isInWater){
                    player.setRemainingAir(player.getMaximumAir());
                }
            }
        }

    }

}
