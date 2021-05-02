package net.dohaw.blackclover.grimmoire.spell.type.compass;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Cartographer extends CastSpellWrapper {

    public Cartographer(GrimmoireConfig grimmoireConfig) {
        super(SpellType.CARTOGRAPHER, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        Player player = pd.getPlayer();
        PlayerInventory playerInv = player.getInventory();
        playerInv.addItem(new ItemStack(Material.FILLED_MAP));
        return true;
    }

    @Override
    public void prepareShutdown() {

    }

}
