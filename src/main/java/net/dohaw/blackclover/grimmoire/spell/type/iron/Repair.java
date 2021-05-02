package net.dohaw.blackclover.grimmoire.spell.type.iron;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DependableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Repair extends CastSpellWrapper implements Listener {

    public Repair(GrimmoireConfig grimmoireConfig) {
        super(SpellType.REPAIR, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player p = pd.getPlayer();
        ItemStack handItem = p.getInventory().getItemInMainHand();
        if (handItem.getType() == Material.AIR) {
            p.sendMessage("There's nothing in your hand to repair");
        }else if(handItem.getType().toString().contains("IRON")){

            ItemMeta handItemMeta = handItem.getItemMeta();
            if(handItemMeta instanceof Damageable){
                Damageable damageable = (Damageable) handItemMeta;
                if (damageable.getDamage() != 0) {
                    damageable.setDamage(0);
                    handItem.setItemMeta((ItemMeta) damageable);
                    p.sendMessage("The item in your hand has been repaired!");
                    SpellUtils.playSound(p, Sound.BLOCK_ANVIL_USE);
                    SpellUtils.playWorldEffect(p, Effect.MOBSPAWNER_FLAMES, 5);
                    return true;
                } else {
                    p.sendMessage("The item in your hand is already repaired");
                }
            }

        }else{
            p.sendMessage("You can only repair iron items");
        }

        SpellUtils.playSound(p, Sound.BLOCK_SLIME_BLOCK_HIT);
        return false;
    }

    @Override
    public void prepareShutdown() {}
}
