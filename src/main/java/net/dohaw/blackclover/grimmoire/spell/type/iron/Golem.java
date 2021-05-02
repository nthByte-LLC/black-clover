package net.dohaw.blackclover.grimmoire.spell.type.iron;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DependableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Golem extends CastSpellWrapper implements Listener {

    private int castDistance;
    // Iron golem and its target
    private HashMap<IronGolem, Entity> golems = new HashMap<>();

    public Golem(GrimmoireConfig grimmoireConfig) {
        super(SpellType.GOLEM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, entityInSight)) {
            org.bukkit.entity.IronGolem golem = (IronGolem) player.getWorld().spawnEntity(player.getLocation(), EntityType.IRON_GOLEM);
            golem.setTarget((LivingEntity) entityInSight);
            golem.setPlayerCreated(true);
            golems.put(golem, entityInSight);
            SpellUtils.playWorldEffect(golem, Effect.MOBSPAWNER_FLAMES);
            SpellUtils.playSound(golem, Sound.ENTITY_HORSE_ARMOR);
            return true;
        }

        return false;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity en = e.getEntity();
        if(en instanceof IronGolem){
            if (golems.containsKey(en)) {
                IronGolem golem = (IronGolem) golems.get(en);
                SpellUtils.playWorldEffect(golem, Effect.SMOKE);
                SpellUtils.playSound(golem, Sound.ENTITY_FOX_DEATH);
                golems.remove(en);
            }
        }
    }

    @EventHandler
    public void onTargetChange(EntityTargetEvent e) {
        Entity potentialGolem = e.getEntity();
        if (potentialGolem instanceof IronGolem) {
            if (golems.containsKey(potentialGolem)) {
                Entity golemTarget = golems.get(potentialGolem);
                Entity eventTarget = e.getTarget();
                if(eventTarget != null){
                    if(!golemTarget.getUniqueId().equals(eventTarget.getUniqueId())){
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

    @Override
    public void prepareShutdown() {}

}
