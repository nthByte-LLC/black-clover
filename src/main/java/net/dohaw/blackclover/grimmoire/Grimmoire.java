package net.dohaw.blackclover.grimmoire;

import lombok.Setter;
import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.Wrapper;
import net.dohaw.blackclover.WrapperHolder;
import net.dohaw.blackclover.grimmoire.type.Anti;
import net.dohaw.blackclover.grimmoire.type.Fire;
import net.dohaw.blackclover.grimmoire.type.Sand;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;

import java.util.ArrayList;
import java.util.List;

public class Grimmoire extends WrapperHolder {

    @Setter
    public static BlackCloverPlugin instance;

    public static final GrimmoireWrapper FIRE = new Fire();
    public static final GrimmoireWrapper SAND = new Sand();
    public static final GrimmoireWrapper ANTI = new Anti();

    public static List<GrimmoireWrapper> getByTier(int tier){
        List<GrimmoireWrapper> validWrappers = new ArrayList<>();
        for(Wrapper wrapper : wrappers.values()){
            GrimmoireWrapper gWrapper = (GrimmoireWrapper) wrapper;
            if(gWrapper.getTier() == tier){
                validWrappers.add(gWrapper);
            }
        }
        return validWrappers;
    }

    public static GrimmoireWrapper getByAlias(String alias){
        for(Wrapper wrapper : wrappers.values()){
            GrimmoireWrapper gWrapper = (GrimmoireWrapper) wrapper;
            if(gWrapper.getAliases().contains(alias.toLowerCase())){
                return gWrapper;
            }
        }
        return null;
    }

    public static BarColor colorCodeToBarColor(String code){
        if(code.equalsIgnoreCase("&4") || code.equalsIgnoreCase("&c")){
            return BarColor.RED;
        }else if(code.equalsIgnoreCase("&6") || code.equalsIgnoreCase("&e")){
            return BarColor.YELLOW;
        }else if(code.equalsIgnoreCase("&2") || code.equalsIgnoreCase("&a")){
            return BarColor.GREEN;
        }else if(code.equalsIgnoreCase("&b") || code.equalsIgnoreCase("&3") || code.equalsIgnoreCase("&1") || code.equalsIgnoreCase("&9")){
            return BarColor.BLUE;
        }else if(code.equalsIgnoreCase("&d")){
            return BarColor.PINK;
        }else if(code.equalsIgnoreCase("&5")){
            return BarColor.PURPLE;
        }else if(code.equalsIgnoreCase("&f") || code.equalsIgnoreCase("&7")){
            return BarColor.WHITE;
        }else{
            return BarColor.BLUE;
        }
    }

}
