package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.gravity.AddWeight;
import net.dohaw.blackclover.grimmoire.spell.type.gravity.Float;
import net.dohaw.blackclover.grimmoire.spell.type.gravity.Levitate;
import net.dohaw.blackclover.grimmoire.spell.type.gravity.RemoveWeight;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.List;

public class Gravity extends GrimmoireWrapper implements Listener {

    public Float floatSpell;
    public AddWeight addWeight;
    public RemoveWeight removeWeight;
    public Levitate levitate;

    public Gravity() {
        super(GrimmoireType.GRAVITY);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("gravity");
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.SPECIAL;
    }

    @Override
    public void initSpells() {

        this.levitate = new Levitate(config);
        this.spells.put(SpellType.LEVITATE, levitate);

        this.removeWeight = new RemoveWeight(config);
        this.spells.put(SpellType.REMOVE_WEIGHT, removeWeight);

        this.addWeight = new AddWeight(config);
        this.spells.put(SpellType.ADD_WEIGHT, addWeight);

        this.floatSpell = new Float(config);
        this.spells.put(SpellType.FLOAT, floatSpell);

    }

    @EventHandler
    public void onTakeFallDamage(EntityDamageEvent e){
        Entity entity = e.getEntity();
        if(e.getCause() == EntityDamageEvent.DamageCause.FALL){
            if(entity instanceof Player){
                Player player = (Player) entity;
                PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player.getUniqueId());
                if(pd.getGrimmoireWrapper().getKEY() == GrimmoireType.GRAVITY){
                    e.setCancelled(true);
                }
            }
        }
    }


}
