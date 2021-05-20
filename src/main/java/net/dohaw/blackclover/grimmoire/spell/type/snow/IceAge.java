package net.dohaw.blackclover.grimmoire.spell.type.snow;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.SnowPlayerData;
import net.dohaw.blackclover.util.BlockSnapshot;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Snowable;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class IceAge extends CastSpellWrapper implements Listener {

    private final double CHANCE_FOR_ICE_SPIKE = 0.3;
    private final Material[] ICE_MATERIALS = {Material.ICE, Material.BLUE_ICE, Material.FROSTED_ICE, Material.PACKED_ICE, Material.SNOW};

    /*
        I keep track of this in case 2 people ice age in the same spot. Need to not change the same blocks twice.
     */
    private HashSet<Location> changedLocations = new HashSet<>();

    private double iceAgeDuration, freezeDuration;
    private int radius;

    public IceAge(GrimmoireConfig grimmoireConfig) {
        super(SpellType.ICE_AGE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        if(pd instanceof SnowPlayerData){

            SnowPlayerData spd = (SnowPlayerData) pd;
            Player player = spd.getPlayer();
            Location start = player.getLocation();
            /*
                Turns nearby water to ice as well as shifts some of the terrain to a snowy look.
             */
            for(double x = start.getX() - radius; x <= start.getX() + radius; x++){
                for(double y = start.getY() - radius; y <= start.getY() + radius; y++){
                    for(double z = start.getZ() - radius; z <= start.getZ() + radius; z++){

                        Location loc = new Location(start.getWorld(), x, y, z);
                        Block block = loc.getBlock();

                        if(!changedLocations.contains(loc)){
                            spd.addChangedIceAgeBlock(block);
                            Material blockType = block.getType();
                            if(block.isLiquid()){
                                if(blockType == Material.WATER){
                                    Material randomIce = ICE_MATERIALS[ThreadLocalRandom.current().nextInt(0, ICE_MATERIALS.length)];
                                    block.setType(randomIce);
                                }
                            }else if(blockType == Material.GRASS_BLOCK){
                                int randNum = ThreadLocalRandom.current().nextInt(0, 2);
                                /*
                                    It either makes snowy grass or ice.
                                 */
                                if(randNum == 0){
                                    Snowable snow = (Snowable) block.getBlockData();
                                    snow.setSnowy(true);
                                    block.setBlockData(snow);
                                }else{
                                    Material randomIce = ICE_MATERIALS[ThreadLocalRandom.current().nextInt(0, ICE_MATERIALS.length)];
                                    block.setType(randomIce);
                                    if(randomIce == Material.SNOW){
                                        Snow snow = (Snow) block.getBlockData();
                                        snow.setLayers(ThreadLocalRandom.current().nextInt(snow.getMinimumLayers(), snow.getMaximumLayers()));
                                        block.setBlockData(snow);
                                    }
                                }
                            }else{
                                continue;
                            }
                            changedLocations.add(loc);

                        }

                    }
                }
            }

            /*
                Freeze nearby entities
             */
            Collection<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
            for(Entity en : nearbyEntities){
                if(en instanceof LivingEntity){
                    SpellUtils.freezeEntity(en, freezeDuration);
                }
            }

            List<BlockSnapshot> changedBlocks = spd.getChangedIceAgeBlocks();
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                changedBlocks.forEach(snap -> {
                    snap.apply();
                    changedLocations.remove(snap.getLocation());
                });

                spd.getChangedIceAgeBlocks().clear();

            }, (long) (iceAgeDuration * 20));

        }else{
            try {
                throw new UnexpectedPlayerData();
            } catch (UnexpectedPlayerData unexpectedPlayerData) {
                unexpectedPlayerData.printStackTrace();
            }
        }


        return true;
    }

    /*
        Prevents ice and snow from melting.
     */
    @EventHandler
    public void onIceMelt(BlockFadeEvent e){
        Block block = e.getBlock();
        if(changedLocations.contains(block.getLocation())){
            e.setCancelled(true);
        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getIntegerSetting(KEY, "Radius");
        this.iceAgeDuration = grimmoireConfig.getDoubleSetting(KEY, "Ice Age Duration");
        this.freezeDuration = grimmoireConfig.getDoubleSetting(KEY, "Freeze Duration");
    }

}
