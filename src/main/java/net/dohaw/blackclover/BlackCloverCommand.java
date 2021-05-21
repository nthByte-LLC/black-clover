package net.dohaw.blackclover;

import net.dohaw.blackclover.config.PlayerDataConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
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

            if(args[0].equalsIgnoreCase("set")) {

                // /bclover set grim <name> <grim>
                if (args[1].equalsIgnoreCase("grim") && (args.length == 4 || args.length == 3) && sender.hasPermission("blackclover.set.grimmoire")) {

                    String alias;
                    Player targetPlayer;

                    // /bclover set grim <name> <grim>
                    if (args.length == 4) {
                        String playerName = args[2];
                        targetPlayer = Bukkit.getPlayer(playerName);
                        alias = args[3];
                    } else {

                        // /bclover set grim <grim>
                        if (!(sender instanceof Player)) {
                            sender.sendMessage("Only player's can use this command!");
                            return false;
                        }
                        targetPlayer = (Player) sender;
                        alias = args[2];

                    }

                    if (targetPlayer != null) {

                        GrimmoireWrapper wrapperFromAlias = Grimmoire.getByAlias(alias);
                        if (wrapperFromAlias != null) {

                            PlayerDataManager pdm = plugin.getPlayerDataManager();
                            UUID potentialPlayerUUID = targetPlayer.getUniqueId();
                            pdm.saveData(potentialPlayerUUID);

                            PlayerData playerData = plugin.getPlayerDataManager().getData(potentialPlayerUUID);
                            PlayerDataConfig dataConfig = playerData.getConfig();
                            dataConfig.getConfig().set("Grimmoire Type", wrapperFromAlias.getKEY().toString());
                            dataConfig.saveConfig();

                            plugin.removeRegenBar(targetPlayer);
                            pdm.removeDataFromMemory(potentialPlayerUUID);

                            pdm.loadData(targetPlayer);
                            playerData.setMaxRegen(plugin.getMaxRegen(wrapperFromAlias.getTier()));
                            playerData.setRegenAmount(0);
                            playerData.setExperience(0);
                            playerData.setLevel(1);
                            playerData.setNumUnusedPoints(0);

                            ItemStack grimmoire = PDCHandler.getGrimmoire(targetPlayer);
                            if (grimmoire != null) {
                                targetPlayer.getInventory().remove(grimmoire);
                            }

                            ItemStack newGrimmoire = BlackCloverPlugin.getBaseGrimmoire();
                            wrapperFromAlias.adaptItemStack(newGrimmoire);

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

                    } else {
                        rf.sendMessage("This is not a valid player!");
                    }

                    // /bclover set level C10_MC 1
                } else if (args[1].equalsIgnoreCase("level") && (args.length == 4 || args.length == 3) && sender.hasPermission("blackclover.set.level")) {

                    String levelArg;
                    Player targetPlayer;
                    if (args.length == 4) {
                        String playerName = args[2];
                        targetPlayer = Bukkit.getPlayer(playerName);
                        levelArg = args[3];
                    } else {
                        if (!(sender instanceof Player)) {
                            sender.sendMessage("Only players can use this command!");
                            return false;
                        }
                        targetPlayer = (Player) sender;
                        levelArg = args[2];
                    }

                    if (targetPlayer == null) {
                        sender.sendMessage("This is not a valid player!");
                        return false;
                    }

                    if (!MathHelper.isInt(levelArg)) {
                        sender.sendMessage("This is not a valid number!");
                        return false;
                    }

                    int level = Integer.parseInt(levelArg);

                    PlayerData targetPlayerData = plugin.getPlayerDataManager().getData(targetPlayer);
                    targetPlayerData.setLevel(level);
                    if (args.length == 4) {
                        sender.sendMessage(net.dohaw.corelib.StringUtils.colorString("This player's level has been set to &b&l" + level));
                    }
                    targetPlayer.sendMessage(net.dohaw.corelib.StringUtils.colorString("Your level has been set to &b&l" + level));

                    // /bclover set xp <player> <num>
                } else if (args[1].equalsIgnoreCase("xp") && (args.length == 4 || args.length == 3) && sender.hasPermission("blackclover.set.xp")) {

                    String xpArg;
                    Player targetPlayer;
                    if (args.length == 4) {
                        String playerName = args[2];
                        targetPlayer = Bukkit.getPlayer(playerName);
                        xpArg = args[3];
                    } else {
                        // /bclover set xp <num>
                        if (!(sender instanceof Player)) {
                            sender.sendMessage("Only players can use this command!");
                            return false;
                        }
                        targetPlayer = (Player) sender;
                        xpArg = args[2];
                    }

                    if (targetPlayer == null) {
                        sender.sendMessage("This is not a valid player!");
                        return false;
                    }

                    if (!MathHelper.isInt(xpArg)) {
                        sender.sendMessage("This is not a valid number!");
                        return false;
                    }

                    int xp = Integer.parseInt(xpArg);

                    PlayerData targetPlayerData = plugin.getPlayerDataManager().getData(targetPlayer);
                    targetPlayerData.setExperience(xp);
                    if (args.length == 4) {
                        sender.sendMessage(net.dohaw.corelib.StringUtils.colorString("This player's XP has been set to &b&l" + xp));
                    }
                    targetPlayer.sendMessage(net.dohaw.corelib.StringUtils.colorString("Your XP has been set to &b&l" + xp));

                }

            }else if(args[0].equalsIgnoreCase("listgrim") && sender.hasPermission("blackclover.list")){
                rf.sendMessage("&0Black&dClover&f Grimmoies:");
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

            }

        }

        return false;

    }




}
