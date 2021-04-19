package net.dohaw.blackclover.playerdata;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FungusPlayerData extends PlayerData{

    private Location morphLocation;

    private ItemStack[] itemsBeforeMorphing;

    private TreeType morphType;

    private List<Location> cactusBlockLocations = new ArrayList<>();

    private boolean isMorphed;

    public FungusPlayerData(UUID uuid) {
        super(uuid, Grimmoire.FUNGUS);
    }

    public boolean isMorphed() {
        return isMorphed;
    }

    public void setMorphed(boolean morphed) {
        isMorphed = morphed;
    }

    public List<Location> getCactusBlockLocations() {
        return cactusBlockLocations;
    }

    public void setCactusBlockLocations(List<Location> cactusBlockLocations) {
        this.cactusBlockLocations = cactusBlockLocations;
    }

    public TreeType getMorphType() {
        return morphType;
    }

    public void setMorphType(TreeType morphType) {
        this.morphType = morphType;
    }

    public ItemStack[] getItemsBeforeMorphing() {
        return itemsBeforeMorphing;
    }

    public void setItemsBeforeMorphing(ItemStack[] itemsBeforeMorphing) {
        this.itemsBeforeMorphing = itemsBeforeMorphing;
    }

    public Location getMorphLocation() {
        return morphLocation;
    }

    public void setMorphLocation(Location morphLocation) {
        this.morphLocation = morphLocation;
    }

}
