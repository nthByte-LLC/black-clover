package net.dohaw.blackclover.grimmoire.spell.type.sand;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.StructureGenerator;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

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
        p.teleport(location);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            SpellUtils.playSound(p, Sound.ITEM_CHORUS_FRUIT_TELEPORT);
            SpellUtils.spawnParticle(p, Particle.END_ROD, 30, 1, 1, 1);
        }, 2);

        return true;
    }

}
