package net.dohaw.blackclover.playerdata;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FungusPlayerData extends PlayerData{

    /*
        These variables are used when the player morphs and we need to store their items before we take them off them so that they can be completely invisible
     */
    private ItemStack[] itemsBeforeMorphing;

    private TreeType typeMorph;

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

    public TreeType getTypeMorph() {
        return typeMorph;
    }

    public void setTypeMorph(TreeType typeMorph) {
        this.typeMorph = typeMorph;
    }

    public ItemStack[] getItemsBeforeMorphing() {
        return itemsBeforeMorphing;
    }

    public void setItemsBeforeMorphing(ItemStack[] itemsBeforeMorphing) {
        this.itemsBeforeMorphing = itemsBeforeMorphing;
    }

}
