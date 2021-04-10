package net.dohaw.blackclover.grimmoire.spell.type.cotton;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.CottonPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Tent extends CastSpellWrapper {

    public Tent(GrimmoireConfig grimmoireConfig) {
        super(SpellType.TENT, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {
        if(pd instanceof CottonPlayerData){
            CottonPlayerData cpd = (CottonPlayerData) pd;
            spawnTent(pd.getPlayer());
        }else{
            throw new UnexpectedPlayerData();
        }
        return false;
    }

    private boolean spawnTent(Player player) {

        File file = new File(Grimmoire.instance.getDataFolder().getAbsolutePath() + "/tent.schem");
        if(file.exists()){

            ClipboardFormat format = ClipboardFormats.findByFile(file);
            if(format != null){

                ClipboardReader reader;
                Clipboard clipboard;

                try{
                    reader = format.getReader(new FileInputStream(file));
                    clipboard = reader.read();
                }catch(IOException e){
                    e.printStackTrace();
                    return false;
                }

                Location playerLocation = player.getLocation();

                ClipboardHolder holder = new ClipboardHolder(clipboard);

                World adaptedWorld = BukkitAdapter.adapt(player.getWorld());
                EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
                // Saves our operation and builds the paste - ready to be completed.
                Operation operation = holder.createPaste(editSession)
                        .to(BlockVector3.at(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ())).ignoreAirBlocks(true).build();

                try { // This simply completes our paste and then cleans up.
                    Operations.complete(operation);
                    editSession.flushSession();
                } catch (WorldEditException e) { // If worldedit generated an exception it will go here
                    player.sendMessage(ChatColor.RED + "OOPS! Something went wrong, please contact an administrator");
                    e.printStackTrace();
                }

            }else{
                Grimmoire.instance.getLogger().severe("There was an error with the ClipboardFormat object in the \"Tent\" spell!");
            }

        }

        return false;

    }

}
