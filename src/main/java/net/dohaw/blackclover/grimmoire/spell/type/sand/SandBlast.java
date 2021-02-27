package net.dohaw.blackclover.grimmoire.spell.type.sand;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.DamageableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.SandBlastRunner;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

public class SandBlast extends CastSpellWrapper implements DamageableSpell {

    private double forceMultiplier;

    public SandBlast(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SAND_BLAST, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        World world = player.getWorld();

        FallingBlock sandBlock = world.spawnFallingBlock(player.getLocation().add(0, 1,0 ), Material.SAND.createBlockData());
        sandBlock.setDropItem(false);
        new SandBlastRunner(player, sandBlock, damageScale).runTaskTimer(Grimmoire.instance, 0L, 1L);
        Vector playerDirection = player.getLocation().getDirection().multiply(forceMultiplier);
        sandBlock.setHurtEntities(false);
        sandBlock.setVelocity(playerDirection);

        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.forceMultiplier = grimmoireConfig.getNumberSetting(KEY, "Force Multiplier");
    }
}
