package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.event.TrapActivationEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.trap.Stun;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.TrapPlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class Trap extends GrimmoireWrapper implements Listener {

    public Stun stun;

    public Trap() {
        super(GrimmoireType.TRAP);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("trap");
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DEFENSE;
    }

    @Override
    public void initSpells() {

        this.stun = new Stun(config);
        this.spells.put(SpellType.STUN, stun);

    }

    @EventHandler
    public void onTrapActivate(TrapActivationEvent e){
        net.dohaw.blackclover.grimmoire.spell.type.trap.Trap trap = e.getTrap();
        TrapPlayerData trapOwnerData = (TrapPlayerData) Grimmoire.instance.getPlayerDataManager().getData(trap.getOwner());
        trapOwnerData.removeTrap(trap);
    }

}
