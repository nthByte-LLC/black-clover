package net.dohaw.blackclover;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.PlayerDataManager;
import net.dohaw.blackclover.util.PDCHandler;
import net.dohaw.corelib.ResponderFactory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BlackCloverCommand implements CommandExecutor {

    private BlackCloverPlugin plugin;

    public BlackCloverCommand(BlackCloverPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        ResponderFactory rf = new ResponderFactory(sender, plugin.getPrefix());

        if(args.length == 3){

            if(args[0].equalsIgnoreCase("setgrim") && sender.hasPermission("blackclover.change.grimmoire")){

                String alias = args[1];
                String playerName = args[2];
                Player potentialPlayer = Bukkit.getPlayer(playerName);
                if(potentialPlayer != null){

                    GrimmoireWrapper wrapperFromAlias = Grimmoire.getByAlias(alias);
                    if(wrapperFromAlias != null){

                        PlayerDataManager pdm = plugin.getPlayerDataManager();
                        PlayerData pd = pdm.getData(potentialPlayer.getUniqueId());
                        GrimmoireWrapper previousGrimmoire = pd.getGrimmoireWrapper();
                        removePreviousGrimmoireItems(potentialPlayer, previousGrimmoire);

                        pd.setGrimmoireWrapper(wrapperFromAlias);
                        pd.setMaxMana(plugin.getMaxMana(wrapperFromAlias.getTier()));
                        pd.setManaAmount(0);

                        ItemStack grimmoire = PDCHandler.getGrimmoire(potentialPlayer);
                        if(grimmoire != null){
                            potentialPlayer.getInventory().remove(grimmoire);
                        }

                        ItemStack newGrimmoire = BlackCloverPlugin.getBaseGrimmoire();
                        wrapperFromAlias.adaptItemStack(newGrimmoire);

                        potentialPlayer.getInventory().setItemInOffHand(newGrimmoire);

                        plugin.removeManaBar(potentialPlayer);
                        pdm.initManaBar(potentialPlayer, wrapperFromAlias);
                        giveNewGrimmoireItems(potentialPlayer, wrapperFromAlias);

                        String newGrimmoireName = wrapperFromAlias.getKEY().toString();
                        if(sender instanceof Player){
                            rf.sendMessage("This player's grimmoire has been changed to " + newGrimmoireName);
                        }

                        ResponderFactory playerResponder = new ResponderFactory(potentialPlayer, plugin.getPrefix());
                        playerResponder.sendMessage("Your grimmoire has been set to " + newGrimmoireName);

                    }else{
                        rf.sendMessage("This is not a valid grimmoire alias!");
                    }

                }else{
                    rf.sendMessage("This is not a valid player!");
                }

            }

        }



        return false;
    }

    private void removePreviousGrimmoireItems(Player player, GrimmoireWrapper wrapper){
        PlayerInventory inv = player.getInventory();
        for(SpellWrapper spell : wrapper.getSpells().values()){
            inv.remove(spell.getSpellBoundItem());
        }
    }

    private void giveNewGrimmoireItems(Player player, GrimmoireWrapper wrapper){
        PlayerInventory inv = player.getInventory();
        for(SpellWrapper spell : wrapper.getSpells().values()){
            inv.setItem(spell.getHotbarSlot(), spell.getSpellBoundItem());
        }
    }

}
