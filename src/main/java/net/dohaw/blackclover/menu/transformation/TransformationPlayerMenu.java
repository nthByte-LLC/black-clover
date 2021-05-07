package net.dohaw.blackclover.menu.transformation;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.corelib.StringUtils;
import net.dohaw.corelib.helpers.ItemStackHelper;
import net.dohaw.corelib.menus.Menu;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class TransformationPlayerMenu extends TransformationMenu {

    private Player morphingPlayer;

    public TransformationPlayerMenu(JavaPlugin plugin, Menu previousMenu, String menuTitle, int numSlots, Player morphingPlayer) {
        super(plugin, previousMenu, menuTitle, numSlots);
        this.morphingPlayer = morphingPlayer;
    }

    @Override
    public void morph(InventoryClickEvent e) {

    }

    @Override
    public CastSpellWrapper getTransformationSpell() {
        return Grimmoire.TRANSFORMATION.monstopher;
    }

    @Override
    public void initializeItems(Player p) {

        for(Player player : Bukkit.getOnlinePlayers()){

            if(player.getUniqueId() != p.getUniqueId()){

                ItemStack playerHead = ItemStackHelper.getPlayerHead(player.getUniqueId());
                String displayName = StringUtils.colorString("&b" + player.getName());
                List<String> lore;
                if(morphingPlayer != null){
                    lore = Collections.singletonList(StringUtils.colorString("&f&lMorph into this player"));
                }else{
                    lore = Collections.singletonList(StringUtils.colorString("&f&lChoose to morph this player"));
                }

                inv.addItem(createGuiItem(playerHead, displayName, lore));

            }

        }

        this.fillerMat = Material.BLACK_STAINED_GLASS_PANE;
        this.backMat = Material.ARROW;
        fillMenu(morphingPlayer != null);

    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        Inventory clickedInventory = e.getClickedInventory();
        Inventory topInventory = player.getOpenInventory().getTopInventory();

        if (clickedInventory == null) return;
        if (!topInventory.equals(inv) || !clickedInventory.equals(topInventory)) return;
        if (clickedItem == null && e.getCursor() == null) return;

        e.setCancelled(true);

        if(clickedItem != null && clickedItem.getItemMeta() != null) {
            String playerHeadName = StringUtils.removeChatColor(clickedItem.getItemMeta().getDisplayName());
            Player playerClicked = Bukkit.getPlayer(playerHeadName);
            if (clickedItem.getType() != fillerMat) {
                //It could be null because they haven't selected the player that is morphing yet.
                if(morphingPlayer != null){
                    System.out.println("CHANGING SKIN");
                    changeSkin(morphingPlayer, playerClicked);
                }else{
                    System.out.println("BRINGING UP OTHER MENU");
                    // Brings up the menu that allows you to choose who the player is morphing into
                    TransformationPlayerMenu newMenu = new TransformationPlayerMenu(plugin, this, "Morph Player " + playerHeadName, 54, playerClicked);
                    newMenu.initializeItems(player);
                    player.closeInventory();
                    newMenu.openInventory(player);
                }
            }
        }

    }

    /**
     *
     * @param morphingPlayer Who is morphing
     * @param targetPlayer The player name you are morphing into
     */
    protected void changeSkin(Player morphingPlayer, Player targetPlayer){

        EntityPlayer morphingPlayerNMS = ((CraftPlayer) morphingPlayer).getHandle();
        GameProfile morphingPlayerProfile = morphingPlayerNMS.getProfile();
        PlayerConnection connection = morphingPlayerNMS.playerConnection;

        for(Player player : Bukkit.getOnlinePlayers()){
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle()));
        }

//        EntityPlayer targetPlayerNMS = ((CraftPlayer) targetPlayer).getHandle();
//        GameProfile targetPlayerProfile = targetPlayerNMS.getProfile();
//        Property targetPlayerProperty = targetPlayerProfile.getProperties().get("textures").iterator().next();

        morphingPlayerProfile.getProperties().removeAll("textures");
        morphingPlayerProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYyMDM5OTg1Njg0MSwKICAicHJvZmlsZUlkIiA6ICJmZDYwZjM2ZjU4NjE0ZjEyYjNjZDQ3YzJkODU1Mjk5YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2I4NzlmZTFlYzQ3ZDE1MzQ1Mjg4NTU2MmU3MmEzMGE0N2YwOTA2NjM0NWVkODYwZDg1MGI2NGM1ZTFmNGY5OTgiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==", "rihlP85I1xJTVtQ2t6StpUktgucdNf3DREuNZ94Did6TQBaykyrbOmHUP3tisR1KcPH0foIzXxE9LR/CrSTFGpT5MZV1WTKGhV2WZHwFIhMbIvw5nVXw5O1pKEhljdrxTGy4rp12jD+sBJDWZeHLgbTuIKoysI70fjQY6JincVFYrxvPQ7bnjvha5rB/bxILtbNCeoF9iudW/rQkGCACy9U8gyryg5iohRjIAtRNp8cjFs9QTFPhrtc3GMVIaHiL774PMKq2Uw+JgQrbb6SmWO5rJYD09+nwtUeGuhPHgaRLnqCqvFFXWrvtlI+HRjuM2YZeoRdMoqULwBA1SLNcTGyPO7UBBIeArxlFFQ1h9R+L9sFT88sywtdPWKT3gkA314Y1csK9zESMmjkoxdTtvlB5y7WIAsLubWFA/Ft6clyIc59n5Ts5in8JyY62Py8+lZptXsO86Er6X4EtLnM+ldAaJpNIi8Trm6HVRRm0zHymWw7MJwuF5ZXK3u8tTfL8c0VZy4fIq21jvKsnCzup3DdWLItCjOXVpkgUh5MkQoCiI+ZP6+wcO0MJ2UVTxq4L4SeGMO/UUIdthdcTzlECNgRIMF1d6ZumK9DRfaB7OAMsr7/z0J38wEzzgMmZ1jm2AoaLNShcc7xfVgqlHJOXXAQDxnDg03n2tGECVqym7B0="));

        for(Player player : Bukkit.getOnlinePlayers()){
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle()));
        }

    }

}
