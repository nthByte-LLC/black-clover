package net.dohaw.blackclover.grimmoire.spell.type.sand;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.runnable.particle.TornadoParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.StructureGenerator;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;

public class AncientRuin extends CastSpellWrapper {

    private final String TEMPLE_PYRAMID_KEY = "desert_pyramid";

    public AncientRuin(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ANCIENT_RUIN, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player p = pd.getPlayer();
        Location playerLocation = p.getLocation();
        BlockPosition bp = new BlockPosition(playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ());
        World world = p.getWorld();
        CraftWorld cWorld = (CraftWorld) world;
        WorldServer worldServer = cWorld.getHandle();

        StructureGenerator<?> desertTemple = StructureGenerator.a.get(TEMPLE_PYRAMID_KEY);
        BlockPosition desertTemplePosition = worldServer.a(desertTemple, bp, 100, false);
        int y = world.getHighestBlockYAt(desertTemplePosition.getX(), desertTemplePosition.getZ());
        Location location = new Location(world, desertTemplePosition.getX(), y, desertTemplePosition.getZ());

        BukkitTask runner = new TornadoParticleRunner(p, new Particle.DustOptions(Color.FUCHSIA, 1), true, 1, false).runTaskTimer(Grimmoire.instance, 0, 1L);
        BukkitTask runner2 = new TornadoParticleRunner(p, new Particle.DustOptions(Color.YELLOW, 1), true, 1, true).runTaskTimer(Grimmoire.instance, 0, 1L);
        BukkitTask runner3 = new CircleParticleRunner(p, new Particle.DustOptions(Color.YELLOW, 1), true, 1).runTaskTimer(Grimmoire.instance, 0, 1L);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            SpellUtils.playSound(p, Sound.ITEM_CHORUS_FRUIT_TELEPORT);
            p.teleport(location);
            runner.cancel();
            runner2.cancel();
            runner3.cancel();
        }, 35);

        return true;
    }

    @Override
    public void prepareShutdown() {

    }
}
