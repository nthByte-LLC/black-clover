package net.dohaw.blackclover.grimmoire.spell.type.trap;

import net.dohaw.blackclover.event.TrapActivationEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.util.BlockSnapshot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A trap that is placed by a player. Usually represented via carpet.
 */
public class Trap {

    private BlockSnapshot previousBlock;
    private TrapSpell spell;
    private UUID owner;
    private Location location;
    private BukkitTask checker;

    public Trap(TrapSpell spell, BlockSnapshot previousBlock, UUID owner, Location location){
        this.location = location;
        this.owner = owner;
        this.spell = spell;
        this.previousBlock = previousBlock;
        initChecker();
    }

    public void removeTrap(){
        this.checker.cancel();
        previousBlock.apply();
    }

    private void initChecker(){
        this.checker = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities(location, 0.5, 1, 0.5);
            for(Entity e : nearbyEntities){
                if(e instanceof LivingEntity){
                    LivingEntity le = (LivingEntity) e;
                    if(!e.getUniqueId().equals(owner)){
                        TrapActivationEvent event = new TrapActivationEvent(this, le);
                        Bukkit.getPluginManager().callEvent(event);
                        spell.onStepOnTrap(this, le);
                    }
                }
            }
        }, 0L, 10L);
    }

    public TrapSpell getSpell() {
        return spell;
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trap trap = (Trap) o;
        return owner.equals(trap.owner) &&
                location.equals(trap.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, location);
    }

}
