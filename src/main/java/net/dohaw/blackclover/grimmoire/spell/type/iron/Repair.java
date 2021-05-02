package net.dohaw.blackclover.grimmoire.spell.type.iron;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DependableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class Repair extends CastSpellWrapper implements DependableSpell, Listener {
    public Repair(GrimmoireConfig grimmoireConfig) {
        super(SpellType.REPAIR, grimmoireConfig);
    }
    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        Player p = pd.getPlayer();
        ItemStack handItem = p.getInventory().getItemInMainHand();
        if (handItem == null || handItem.getType() == Material.AIR) {
            p.sendMessage("There's nothing in your hand to repair");
            p.playSound(p.getLocation(), Sound.BLOCK_SLIME_BLOCK_HIT, 10, 10);
            return false;
        }
        if (handItem.getType().toString().contains("IRON")) {
            if (handItem.getDurability() != 0) {
                handItem.setDurability((short)0);
                p.sendMessage("The item in your hand has been repaired!");
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 10, 10);
                p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
                return false;
            } else {
                p.sendMessage("The item in your hand is already repaired");
            }
        } else {
            p.sendMessage("You can only repair iron items");
        }
        p.playSound(p.getLocation(), Sound.BLOCK_SLIME_BLOCK_HIT, 10, 10);
        return false;
    }

    @Override
    public void initDependableData() {}

    @Override
    public void prepareShutdown() {}
}
