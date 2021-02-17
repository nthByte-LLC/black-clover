package net.dohaw.blackclover.listener;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.config.BaseConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.util.NBTHandler;
import net.dohaw.corelib.ProbabilityUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerWatcher implements Listener {

    private BlackCloverPlugin plugin;

    public PlayerWatcher(BlackCloverPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        if(!NBTHandler.hasGrimmoire(e.getPlayer())){
            e.getPlayer().getInventory().addItem(getRandomGrimmoire());
        }else{
            System.out.println("Has grimmoire");
        }

    }

    @EventHandler
    public void onCastSpell(PlayerInteractEvent e){



    }

    private ItemStack ensureProperHotbar(){

        return null;

    }

    private ItemStack getRandomGrimmoire(){

        List<Integer> tiersAvailable = new ArrayList<>();
        tiersAvailable.add(2);
        tiersAvailable.add(3);

        BaseConfig baseConfig = plugin.getBaseConfig();
        boolean isTierFourAvailable = baseConfig.isGrimmoireTierAvailable(4);
        if(isTierFourAvailable){
            tiersAvailable.add(4);
        }

        boolean isTierFiveAvailable = baseConfig.isGrimmoireTierAvailable(5);
        if(isTierFiveAvailable){
            tiersAvailable.add(5);
        }

        ProbabilityUtilities pu = new ProbabilityUtilities();
        for(int tier : tiersAvailable){
            int obtainingChance = baseConfig.getObtainingChance(tier);
            pu.addChance(tier, obtainingChance);
        }

        int randomTier = (int) pu.getRandomElement();
        List<GrimmoireWrapper> tierWrappers = Grimmoire.getByTier(randomTier);

        Random rand = new Random();
        GrimmoireWrapper randomGrimmoire = tierWrappers.get(rand.nextInt(tierWrappers.size()));

        ItemStack baseGrimmoire = BlackCloverPlugin.getBaseGrimmoire();
        randomGrimmoire.adaptItemStack(baseGrimmoire);

        if(baseConfig.isInTestingMode()){
            System.out.println("Random Tier: " + randomTier);
            System.out.println("Grimmoire Acquired: " + randomGrimmoire.getKEY());
        }

        return baseGrimmoire;

    }

}
