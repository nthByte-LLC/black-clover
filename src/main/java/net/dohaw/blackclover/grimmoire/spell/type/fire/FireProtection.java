package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.PassiveSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.List;

public class FireProtection extends PassiveSpellWrapper implements Listener {

    private final List<SpellType> IMMUNE_SPELLS = Arrays.asList(SpellType.FIRE_BALL, SpellType.FIRE_BLAST, SpellType.FIRE_STORM, SpellType.FIRE_FISTS);

    public FireProtection(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_PROTECTION, grimmoireConfig);
    }

    /*
        Cancels any damage having to do with fire spells.
     */
    @EventHandler
    public void onPlayerTakeSpellDamage(SpellDamageEvent e){

        Entity eDamaged = e.getDamaged();
        if(eDamaged instanceof Player){
            Player player = (Player) eDamaged;
            PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
            if(pd.getGrimmoireWrapper().getKEY() == GrimmoireType.FIRE){
                if(IMMUNE_SPELLS.contains(e.getSpell())){
                    e.setCancelled(true);
                }
            }
        }

    }

    /*
        Cancels any fire damage
     */
    @EventHandler
    public void onPlayerTakeFireDamage(EntityDamageEvent e){

        Entity eDamaged = e.getEntity();
        if(eDamaged instanceof Player){
            Player damaged = (Player) eDamaged;
            PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(damaged.getUniqueId());
            if(pd.getGrimmoireWrapper().getKEY() == GrimmoireType.FIRE){
                if(e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK){
                    e.setCancelled(true);
                }
            }
        }

    }

    @Override
    public void prepareShutdown() {

    }
}
