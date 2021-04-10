package net.dohaw.blackclover.grimmoire.spell.type.cotton;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.CottonPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BlockSnapshot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Bed extends CastSpellWrapper {

    private int duration;

    public Bed(GrimmoireConfig grimmoireConfig) {
        super(SpellType.BED, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        if(pd instanceof CottonPlayerData){
            CottonPlayerData cpd = (CottonPlayerData) pd;
            Player player = pd.getPlayer();
            if(!cpd.isBedSpawned()){
                Block block = player.getLocation().getBlock();
                BlockSnapshot blockSnapshot = BlockSnapshot.toSnapshot(block);
                setBed(block, player.getFacing());
                Bukkit.getScheduler().runTaskLater(Grimmoire.instance, blockSnapshot::apply, duration * 20);
                return true;
            }else{
                player.sendMessage("You already have a bed spawned!");
                return false;
            }
        }else{
            try{
                throw new UnexpectedPlayerData();
            } catch (UnexpectedPlayerData unexpectedPlayerData) {
                unexpectedPlayerData.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getIntegerSetting(KEY, "Duration");
    }

    @Override
    public void prepareShutdown() {

    }

    public void setBed(Block start, BlockFace facing) {
        for (org.bukkit.block.data.type.Bed.Part part : org.bukkit.block.data.type.Bed.Part.values()) {
            start.setBlockData(Bukkit.createBlockData(Material.WHITE_BED, data -> {
                ((org.bukkit.block.data.type.Bed)data).setPart(part);
                ((org.bukkit.block.data.type.Bed)data).setFacing(facing);
            }));
            start = start.getRelative(facing.getOppositeFace());
        }
    }

}
