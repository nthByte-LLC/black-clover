package net.dohaw.blackclover.listener;

import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.config.BaseConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.util.PDCHandler;
import net.dohaw.corelib.ProbabilityUtilities;
import net.dohaw.corelib.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
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
        plugin.getPlayerDataManager().loadData(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onCastSpell(PlayerInteractEvent e){



    }

    private ItemStack ensureProperHotbar(){

        return null;

    }

}
