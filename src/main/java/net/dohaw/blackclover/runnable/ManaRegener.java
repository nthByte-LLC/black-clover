package net.dohaw.blackclover.runnable;

import lombok.SneakyThrows;
import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.config.BaseConfig;
import net.dohaw.blackclover.exception.GrimmoireWrapperNotFoundException;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
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
            if(pdc.has(PDCHandler.MANA_PDC, PersistentDataType.INTEGER)){
                if(PDCHandler.hasGrimmoire(player)){

                    int manaAmount = PDCHandler.getMana(player);
                    GrimmoireWrapper grimmoireWrapper = PDCHandler.getGrimmoireWrapper(player);
                    if(grimmoireWrapper != null){

                        int tier = grimmoireWrapper.getTier();
                        int maxTierMana = plugin.getMaxMana(tier);

                        if(maxTierMana != manaAmount){

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
                                throw new IllegalArgumentException("Invalid tier found in PDC!");
                            }

                            manaAmount += manaAmountAdditive;

                            if(manaAmount > plugin.getMaxMana(tier)){
                                manaAmount = maxTierMana;
                            }

                            System.out.println("MANA AMOUNT: " + manaAmount);

                            pdc.set(PDCHandler.MANA_PDC, PersistentDataType.INTEGER, manaAmount);

                        }

                    }else{
                        throw new GrimmoireWrapperNotFoundException();
                    }
                }else{
                    plugin.getLogger().warning("A player somehow has the Mana PDC, but not a grimmoire...");
                }
            }
        }
    }

    private void updateManaBar(Player player, GrimmoireWrapper wrapper, int manaAmount){
        BossBar manaBar = plugin.getManaBars().get(player.getUniqueId());
        int maxManaAmount =
        manaBar.setTitle(StringUtils.colorString("&bMana: &f" + manaAmount + "/" + plugin.getMaxMana(wrapper.getTier())));
    }

}
