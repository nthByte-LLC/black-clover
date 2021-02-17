package net.dohaw.blackclover.grimmoire;
import lombok.Getter;
import lombok.NonNull;
import net.dohaw.blackclover.Wrapper;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.corelib.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.List;

public abstract class GrimmoireWrapper extends Wrapper {

    @Getter
    protected GrimmoireConfig config;

    public GrimmoireWrapper(final GrimmoireType TYPE){
        super(TYPE);
        this.config = new GrimmoireConfig("grimmoires" + File.separator + TYPE.toString().toLowerCase() + ".yml");
    }

    public abstract List<String> getAliases();

    public abstract int getTier();

    public abstract GrimmoireClassType getClassType();

    /**
     * Edits the itemstack properties based on what the grimmoire is
     * @param baseGrimmoire The base grimmoire that is unedited.
     */
    public void adaptItemStack(@NonNull ItemStack baseGrimmoire){

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


    }

}
