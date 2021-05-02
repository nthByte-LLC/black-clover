package net.dohaw.blackclover.grimmoire.spell.type.fire;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class FireControl extends CastSpellWrapper {

    public FireControl(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FIRE_CONTROL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        if(e instanceof PlayerInteractEvent){

            PlayerInteractEvent pie = (PlayerInteractEvent) e;
            Player player = pd.getPlayer();
            Block blockRightClicked = pie.getClickedBlock();
            //Extinguishing fires
            if(player.isSneaking()){
                if(blockRightClicked != null){
                    if(blockRightClicked.getType() == Material.FIRE){
                        blockRightClicked.setType(Material.AIR);
                        SpellUtils.playSound(blockRightClicked, Sound.BLOCK_FIRE_EXTINGUISH);
                        SpellUtils.spawnParticle(blockRightClicked, Particle.CRIMSON_SPORE, 10, 0.5f, 0.5f, 0.5f);
                    }
                }else{
                    return false;
                }
            //Creating fires
            }else{

                if(blockRightClicked != null){
                    Block blockAbove = blockRightClicked.getRelative(BlockFace.UP);
                    blockAbove.setType(Material.FIRE);
                    SpellUtils.playSound(blockRightClicked, Sound.BLOCK_FIRE_AMBIENT);
                    SpellUtils.spawnParticle(blockRightClicked, Particle.FLAME, 10, 0.5f, 0.5f, 0.5f);
                }else{
                    return false;
                }

            }

        }

        return true;

    }

    @Override
    public void prepareShutdown() {

    }
}
