package net.dohaw.blackclover.grimmoire.spell.type.dark;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.BlackHoleRunner;
import net.dohaw.blackclover.util.BlockSnapshot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class BlackHole extends CastSpellWrapper {

    private List<BlockSnapshot> previousBlocks = new ArrayList<>();

    private int castDistance;
    private int maxCoordinateAdditive;
    private double reach;
    private double duration;
    private double forceMultiplier;

    public BlackHole(GrimmoireConfig grimmoireConfig) {
        super(SpellType.BLACK_HOLE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player caster = pd.getPlayer();
        Block blockAimingAt = caster.getTargetBlockExact(castDistance);

        if(blockAimingAt != null){

            List<BlockSnapshot> blocksBeforeBlackHole = makeBlackHolePatch(blockAimingAt);
            this.previousBlocks.addAll(blocksBeforeBlackHole);

            BlackHoleRunner blackHoleRunner = new BlackHoleRunner(caster, this, blockAimingAt.getLocation());
            blackHoleRunner.runTaskTimer(Grimmoire.instance, 0L, 10L);

            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                blocksBeforeBlackHole.forEach(BlockSnapshot::apply);
                blackHoleRunner.cancel();
                previousBlocks.removeAll(blocksBeforeBlackHole);
            }, (long) (duration * 20));

            return true;

        }else{
            caster.sendMessage("There isn't a block within a reasonable distance from you!");
        }

        return false;
    }

    @Override
    public void prepareShutdown() {
        for(BlockSnapshot snapshot : previousBlocks){
            snapshot.apply();
        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.reach = grimmoireConfig.getDoubleSetting(KEY, "Reach");
        this.maxCoordinateAdditive = grimmoireConfig.getIntegerSetting(KEY, "Teleport Maximum Additive");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
        this.forceMultiplier = grimmoireConfig.getDoubleSetting(KEY, "Force Multiplier");
    }

    private List<BlockSnapshot> makeBlackHolePatch(Block firstBlock){

        List<BlockSnapshot> previousBlocks = new ArrayList<>();
        List<Block> blackHoleBlocks = new ArrayList<>();

        blackHoleBlocks.add(firstBlock.getRelative(BlockFace.EAST));
        blackHoleBlocks.add(firstBlock.getRelative(BlockFace.SOUTH));
        blackHoleBlocks.add(blackHoleBlocks.get(0).getRelative(BlockFace.SOUTH));
        blackHoleBlocks.add(firstBlock);

        for(Block block : blackHoleBlocks){
            previousBlocks.add(BlockSnapshot.toSnapshot(block));
            block.setType(Material.BLACK_CONCRETE);
        }

        return previousBlocks;

    }

    public double getReach() {
        return reach;
    }

    public int getMaxCoordinateAdditive() {
        return maxCoordinateAdditive;
    }

    public double getForceMultiplier() {
        return forceMultiplier;
    }

}
