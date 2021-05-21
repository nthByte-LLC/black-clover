package net.dohaw.blackclover.util;

import net.dohaw.blackclover.XPGainType;
import net.dohaw.blackclover.config.BaseConfig;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.corelib.StringUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Deals with progress and XP
 */
public class ProgressSystem {

    private static BaseConfig baseConfig;

    public static boolean hasLeveledUp(PlayerData playerData){
        double increasePerLevel = baseConfig.getXPIncreasePerLevel();
        int currentLevel = playerData.getLevel();
        double xpNeededForAdvance = currentLevel * increasePerLevel;
        return xpNeededForAdvance <= playerData.getExperience();
    }

    public static void levelUp(PlayerData playerData){

        int currentLevel = playerData.getLevel();
        int newLevel = playerData.getLevel() + 1;
        double xpThreshold = currentLevel * baseConfig.getXPIncreasePerLevel();
        double leftOverXP = playerData.getExperience() - xpThreshold;

        playerData.setLevel(newLevel);
        playerData.setExperience(leftOverXP);

        Player playerWhoLevel = playerData.getPlayer();
        playerWhoLevel.sendMessage(StringUtils.colorString("You have leveled up to level &b&l" + newLevel + "!"));
        celebrate(playerWhoLevel);

    }

    private static void celebrate(Player playerWhoLeveled){
        SpellUtils.playSound(playerWhoLeveled, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE);
        SpellUtils.playSound(playerWhoLeveled, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR);
        SpellUtils.spawnParticle(playerWhoLeveled, Particle.FIREWORKS_SPARK, 30, 1, 1, 1);
    }

    public static void increaseExperience(PlayerData playerData){
        double xpGained = baseConfig.getXPGained(XPGainType.SPELL_CAST);
        playerData.increaseXP(xpGained);
        System.out.println("XP: " + playerData.getExperience());
        if(ProgressSystem.hasLeveledUp(playerData)){
            ProgressSystem.levelUp(playerData);
        }
    }

    public static void setBaseConfig(BaseConfig config){
        baseConfig = config;
    }

}
