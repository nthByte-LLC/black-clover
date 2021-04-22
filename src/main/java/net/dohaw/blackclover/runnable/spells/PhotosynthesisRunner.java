package net.dohaw.blackclover.runnable.spells;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PhotosynthesisRunner extends BukkitRunnable {

    private double healAmount;
    private int requiredLightSource;

    public PhotosynthesisRunner(int requiredLightSource, double healAmount){
        this.requiredLightSource = requiredLightSource;
        this.healAmount = healAmount;
    }

    @Override
    public void run() {

        for(Player player : Bukkit.getOnlinePlayers()){
            PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
            if(pd.getGrimmoireWrapper().getKEY() == GrimmoireType.FUNGUS){
                Block blockUnderPlayer = player.getLocation().getBlock();
                byte lightSource = blockUnderPlayer.getLightFromSky();
                System.out.println("LIGHT SOURCE: " + lightSource);
                if(requiredLightSource <= lightSource){
                    double alteredHealth = SpellUtils.alterHealth(player, healAmount);
                    System.out.println("ALTERED: " + alteredHealth);
                    if(alteredHealth != 0){
                        SpellUtils.spawnParticle(player, Particle.VILLAGER_HAPPY, 30, 1, 1, 1);
                        SpellUtils.playSound(player, Sound.ENTITY_VILLAGER_CELEBRATE);
                    }
                }
            }
        }

    }

}
