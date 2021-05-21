package net.dohaw.blackclover.grimmoire;

import lombok.Getter;
import lombok.NonNull;
import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.Wrapper;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.DependableSpell;
import net.dohaw.blackclover.grimmoire.spell.PersistableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.PDCHandler;
import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.StringUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GrimmoireWrapper extends Wrapper<GrimmoireType> {

    /**
     * The slot number for each spell bound item and the spell itself.
     */
    @Getter
    protected Map<SpellType, SpellWrapper> spells = new HashMap<>();

    @Getter
    protected GrimmoireConfig config;

    public GrimmoireWrapper(final GrimmoireType TYPE){
        super(TYPE);
        this.config = new GrimmoireConfig("grimmoires" + File.separator + TYPE.toString().toLowerCase() + ".yml");
        initSpells();
    }

    //TODO: Implement PersistableSpell in spells that have persistable data. Also get rid of the SpellWrapper#prepareShutdown method entirely.
    public void shutdown(){
        for(SpellWrapper spell : spells.values()){
            if(spell instanceof PersistableSpell){
                ((PersistableSpell) spell).prepareShutdown();
            }
        }
    }

    /**
     * Other names for this grimmoire. Usually for identifying a grimmoire within all the grimmoires via a command alias.
     * @return The aliases/nicknames
     */
    public abstract List<String> getAliases();

    /**
     * Tier of the grimmoire
     * @return The tier number
     */
    public abstract int getTier();

    /**
     * The class of the grimmoire
     * @return The class
     */
    public abstract GrimmoireClassType getClassType();

    public abstract void initSpells();

    /**
     * Edits the itemstack properties based on what the grimmoire is
     * Also writes in the grimmoire book
     * @param playerData The data of the player that will have this grimmoire
     * @param baseGrimmoire The base grimmoire that is unedited.
     */
    public void adaptItemStack(PlayerData playerData, @NonNull ItemStack baseGrimmoire){

        ItemMeta meta = baseGrimmoire.getItemMeta();
        String displayName = meta.getDisplayName();
        String typeName = KEY.toString();
        displayName = StringUtils.colorString(config.getDisplayNameColor() + displayName.replace("%name%", typeName));
        meta.setDisplayName(displayName);

        List<String> lore = meta.getLore();
        for (int i = 0; i < lore.size(); i++) {

            String line = lore.get(i);
            int tier = getTier();
            line = line.replace("%tierNum%", tier + "");

            if(line.contains("%rarity%")){

                String rarity;
                if(tier == 2){
                    rarity = "&bCommon";
                }else if(tier == 3){
                    rarity = "&1Rare";
                }else if(tier == 4){
                    rarity = "&dUltra";
                }else{
                    rarity = "&6Legendary";
                }
                rarity = StringUtils.colorString(rarity);
                line = line.replace("%rarity%", rarity);

            }

            String className = org.apache.commons.lang.StringUtils.capitalize(getClassType().toString().toLowerCase());
            line = line.replace("%class%", className);

            lore.set(i, line);

        }

        lore = StringUtils.colorLore(lore);

        meta.setLore(lore);
        baseGrimmoire.setItemMeta(meta);

        PDCHandler.markGrimmoire(baseGrimmoire, this.getKEY());

    }


    private String getCenteredHeader(){

        String txt = getKEY().toString() + " Magic";
        //Don't know why this is wrong or why this works. Just added 4 and that centers the text.
        int headerMargin = (19 - txt.length() + 4) / 2;
        String headerMarginSpace = "";

        for (int i = 0; i <= headerMargin; i++) {
            headerMarginSpace += " ";
        }

        return headerMarginSpace + txt + headerMarginSpace;

    }

}
