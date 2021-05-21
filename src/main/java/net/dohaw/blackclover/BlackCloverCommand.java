package net.dohaw.blackclover;

import net.dohaw.blackclover.config.PlayerDataConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.PlayerDataManager;
import net.dohaw.blackclover.util.PDCHandler;
import net.dohaw.corelib.ResponderFactory;
import net.dohaw.corelib.helpers.MathHelper;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.Map;
import java.util.UUID;

public class BlackCloverCommand implements CommandExecutor {

    private BlackCloverPlugin plugin;

    public BlackCloverCommand(BlackCloverPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        ResponderFactory rf = new ResponderFactory(sender, plugin.getPrefix());
        if(args.length > 0){

            if(args[0].equalsIgnoreCase("set") && (args.length == 3 || args.length == 4) ) {

                String setValue;
                Player targetPlayer;

                // /bclover set grim <name> <grim>
                if (args.length == 4) {
                    String playerName = args[2];
                    targetPlayer = Bukkit.getPlayer(playerName);
                    setValue = args[3];
                } else {

                    // /bclover set grim <grim>
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("Only player's can use this command!");
                        return false;
                    }
                    targetPlayer = (Player) sender;
                    setValue = args[2];

                }

                if(targetPlayer == null){
                    rf.sendMessage("This is not a valid player!");
                }

                PlayerData targetPlayerData = plugin.getPlayerDataManager().getData(targetPlayer);

                // /bclover set grim <name> <grim>
                if (args[1].equalsIgnoreCase("grim") && sender.hasPermission("blackclover.set.grimmoire")) {

                    GrimmoireWrapper wrapperFromAlias = Grimmoire.getByAlias(setValue);
                    if (wrapperFromAlias != null) {

                        PlayerDataManager pdm = plugin.getPlayerDataManager();
                        UUID potentialPlayerUUID = targetPlayer.getUniqueId();
                        pdm.saveData(potentialPlayerUUID);

                        PlayerDataConfig dataConfig = targetPlayerData.getConfig();
                        dataConfig.getConfig().set("Grimmoire Type", wrapperFromAlias.getKEY().toString());
                        dataConfig.saveConfig();

                        plugin.removeRegenBar(targetPlayer);
                        pdm.removeDataFromMemory(potentialPlayerUUID);

                        pdm.loadData(targetPlayer);
                        targetPlayerData.setMaxRegen(plugin.getMaxRegen(wrapperFromAlias.getTier()));
                        targetPlayerData.setRegenAmount(0);
                        targetPlayerData.setExperience(0);
                        targetPlayerData.setLevel(1);
                        targetPlayerData.setNumUnusedPoints(0);
                        targetPlayerData.setUnlockedSpells(EnumSet.noneOf(SpellType.class));

                        ItemStack grimmoire = PDCHandler.getGrimmoire(targetPlayer);
                        if (grimmoire != null) {
                            targetPlayer.getInventory().remove(grimmoire);
                        }

                        ItemStack newGrimmoire = BlackCloverPlugin.getBaseGrimmoire();
                        wrapperFromAlias.adaptItemStack(targetPlayerData, newGrimmoire);

                        targetPlayer.getInventory().setItemInOffHand(newGrimmoire);

                        String newGrimmoireName = wrapperFromAlias.getKEY().toString();
                        String grimmoireColorCode = wrapperFromAlias.getConfig().getDisplayNameColor();
                        if (sender instanceof Player) {
                            rf.sendMessage("This player's grimmoire has been changed to " + grimmoireColorCode + newGrimmoireName);
                        }

                        ResponderFactory playerResponder = new ResponderFactory(targetPlayer, plugin.getPrefix());
                        playerResponder.sendMessage("Your grimmoire has been set to " + grimmoireColorCode + newGrimmoireName);

                    } else {
                        rf.sendMessage("This is not a valid grimmoire alias!");
                    }

                // /bclover set level C10_MC 1
                } else if (args[1].equalsIgnoreCase("level") && sender.hasPermission("blackclover.set.level")) {

                    if (!MathHelper.isInt(setValue)) {
                        sender.sendMessage("This is not a valid number!");
                        return false;
                    }

                    int level = Integer.parseInt(setValue);

                    targetPlayerData.setLevel(level);
                    if (args.length == 4) {
                        sender.sendMessage(net.dohaw.corelib.StringUtils.colorString("This player's level has been set to &b&l" + level));
                    }
                    targetPlayer.sendMessage(net.dohaw.corelib.StringUtils.colorString("Your level has been set to &b&l" + level));

                    // /bclover set xp <player> <num>
                } else if (args[1].equalsIgnoreCase("xp") && sender.hasPermission("blackclover.set.xp")) {

                    if (!MathHelper.isInt(setValue)) {
                        sender.sendMessage("This is not a valid number!");
                        return false;
                    }

                    int xp = Integer.parseInt(setValue);

                    targetPlayerData.setExperience(xp);
                    if (args.length == 4) {
                        sender.sendMessage(net.dohaw.corelib.StringUtils.colorString("This player's XP has been set to &b&l" + xp));
                    }
                    targetPlayer.sendMessage(net.dohaw.corelib.StringUtils.colorString("Your XP has been set to &b&l" + xp));

                }else if(args[1].equalsIgnoreCase("points")){

                    if (!MathHelper.isInt(setValue)) {
                        sender.sendMessage("This is not a valid number!");
                        return false;
                    }

                    int points = Integer.parseInt(setValue);

                    targetPlayerData.setNumUnusedPoints(points);
                    if (args.length == 4) {
                        sender.sendMessage(net.dohaw.corelib.StringUtils.colorString("This player's points have been set to &b&l" + points));
                    }
                    targetPlayer.sendMessage(net.dohaw.corelib.StringUtils.colorString("Your points has been set to &b&l" + points));

                }

            }else if(args[0].equalsIgnoreCase("listgrim") && sender.hasPermission("blackclover.list")){
                rf.sendCenteredMessage("&f========== &0Black&dClover&f Grimmoires &f==========");
                Map<Enum, Wrapper> grimmoires = Grimmoire.wrappers;
                for(Enum key : grimmoires.keySet()){
                    if(key instanceof GrimmoireType){
                        GrimmoireType type = (GrimmoireType) key;
                        String formalName = StringUtils.capitalize(type.toString().toLowerCase());
                        sender.sendMessage(net.dohaw.corelib.StringUtils.colorString("> " + formalName));
                    }
                }
            }else if(args[0].equalsIgnoreCase("info") && (args.length == 2 || args.length == 1) ){

                if(!sender.hasPermission("blackclover.info.self") && !sender.hasPermission("blackclover.info.others")){
                    rf.sendMessage("You do not have permission to use this command!");
                    return false;
                }

                Player targetPlayer;
                if(args.length == 1){

                    if(!(sender instanceof Player)){
                        rf.sendMessage("Only players can use this command!");
                        return false;
                    }
                    targetPlayer = (Player) sender;

                }else{
                    String playerNameArg = args[1];
                    targetPlayer = Bukkit.getPlayer(playerNameArg);
                }

                if(targetPlayer == null){
                    rf.sendMessage("This is not a valid player!");
                    return false;
                }

                PlayerData playerData = plugin.getPlayerDataManager().getData(targetPlayer);
                int level = playerData.getLevel();
                double xpLevelingThreshold = plugin.getBaseConfig().getXPIncreasePerLevel() * level;

                rf.sendCenteredMessage("&a&l====== Player Info: ======");

                rf = new ResponderFactory(sender, null);
                rf.sendMessage("&aGrimmoire > &f&l" + playerData.getGrimmoireType().toString());
                rf.sendMessage("&aExperience > &f&l" + playerData.getExperience() + "/" + xpLevelingThreshold);
                rf.sendMessage("&aLevel > &f&l" + level);
                rf.sendMessage("&aUnused Points > &f&l" + playerData.getNumUnusedPoints());

            }

        }

        return false;

    }




}
