package net.dohaw.blackclover.runnable;

import lombok.SneakyThrows;
import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.config.BaseConfig;
import net.dohaw.blackclover.exception.GrimmoireWrapperNotFoundException;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.PlayerDataManager;
import net.dohaw.blackclover.util.PDCHandler;
import net.dohaw.corelib.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class ManaRegener extends BukkitRunnable {

    private BlackCloverPlugin plugin;
    private int baseRegenAmount;
    private int t2RegenMult, t3RegenMult, t4RegenMult, t5RegenMult;

    public ManaRegener(BlackCloverPlugin plugin){
        this.plugin = plugin;
        BaseConfig baseConfig = plugin.getBaseConfig();
        this.baseRegenAmount = baseConfig.getBaseRegen();
        this.t2RegenMult = baseConfig.getTierManaRegenMultiplier(2);
        this.t3RegenMult = baseConfig.getTierManaRegenMultiplier(3);
        this.t4RegenMult = baseConfig.getTierManaRegenMultiplier(4);
        this.t5RegenMult = baseConfig.getTierManaRegenMultiplier(5);
    }

    @SneakyThrows
    @Override
    public void run() {

        for(Player player : Bukkit.getOnlinePlayers()){

            PersistentDataContainer pdc = player.getPersistentDataContainer();
            if(PDCHandler.hasGrimmoire(player)){

                PlayerDataManager pdm = plugin.getPlayerDataManager();
                PlayerData pd = pdm.getData(player.getUniqueId());
                int manaAmount = pd.getManaAmount();
                GrimmoireWrapper grimmoireWrapper = pd.getGrimmoireWrapper();
                if(grimmoireWrapper != null){

                    int tier = grimmoireWrapper.getTier();
                    int maxMana = pd.getMaxMana();

                    if(maxMana != manaAmount){

                        int manaAmountAdditive;
                        if(tier == 2){
                            manaAmountAdditive = baseRegenAmount * t2RegenMult;
                        }else if(tier == 3){
                            manaAmountAdditive = baseRegenAmount * t3RegenMult;
                        }else if(tier == 4){
                            manaAmountAdditive = baseRegenAmount * t4RegenMult;
                        }else if(tier == 5){
                            manaAmountAdditive = baseRegenAmount * t5RegenMult;
                        }else{
                            throw new IllegalArgumentException("Invalid tier!");
                        }

                        manaAmount += manaAmountAdditive;

                        if(manaAmount > maxMana){
                            manaAmount = maxMana;
                        }

                        System.out.println("MANA AMOUNT: " + manaAmount);
                        pd.setMaxMana(manaAmount);
                        updateManaBar(pd);

                    }

                }else{
                    throw new GrimmoireWrapperNotFoundException();
                }
            }

        }

    }

    private void updateManaBar(PlayerData pd){
        BossBar manaBar = plugin.getManaBars().get(pd.getUuid());
        int manaAmount = pd.getManaAmount();
        int maxMana = pd.getMaxMana();
        manaBar.setTitle(StringUtils.colorString("&bMana: &f" + manaAmount + "/" + maxMana));
    }

}
