package net.dohaw.blackclover.grimmoire;

import lombok.Setter;
import net.dohaw.blackclover.BlackCloverPlugin;
import net.dohaw.blackclover.Wrapper;
import net.dohaw.blackclover.WrapperHolder;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import net.dohaw.blackclover.grimmoire.type.*;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.boss.BarColor;

import java.util.ArrayList;
import java.util.List;

public class Grimmoire extends WrapperHolder {

    @Setter
    public static BlackCloverPlugin instance;

    public static final GrimmoireWrapper FIRE = new Fire();
    public static final GrimmoireWrapper SAND = new Sand();
    public static final Anti ANTI = new Anti();
    public static final GrimmoireWrapper SHAKUDO = new Shakudo();
    public static final GrimmoireWrapper WATER = new Water();
    public static final Plant PLANT = new Plant();
    public static final GrimmoireWrapper SNOW = new Snow();
    public static final GrimmoireWrapper ASH = new Ash();
    public static final GrimmoireWrapper COTTON = new Cotton();
    public static final GrimmoireWrapper ROCK = new Rock();
    public static final Gravity GRAVITY = new Gravity();
    public static final Poison POISON = new Poison();
    public static final Fungus FUNGUS = new Fungus();
    public static final Compass COMPASS = new Compass();
    public static final GrimmoireWrapper WIND = new Wind();
    public static final Lightning LIGHTNING = new Lightning();
    public static final GrimmoireWrapper TIME = new Time();
    public static final GrimmoireWrapper IRON = new Iron();
    public static final GrimmoireWrapper TRAP = new Trap();
    public static final Transformation TRANSFORMATION = new Transformation();
    public static final Vortex VORTEX = new Vortex();
    public static final GrimmoireWrapper GLASS = new Glass();
    public static final GrimmoireWrapper DARK = new Dark();
    public static final GrimmoireWrapper SPATIAL = new Spatial();

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

    public static CastSpellWrapper getSpellBoundToSlot(PlayerData pd, int slot){
        GrimmoireWrapper grimmoireWrapper = pd.getGrimmoireWrapper();
        for(SpellWrapper spell : grimmoireWrapper.getSpells().values()){
            if(spell instanceof CastSpellWrapper){
                CastSpellWrapper cSpell = (CastSpellWrapper) spell;
                if(cSpell.isSpellBoundSlot(slot)){
                    return cSpell;
                }
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

    /*
     * Need to do this because most spells rely on Bukkit#getScheduler#runTaskLater. If the server crashes or is shut down when this task hasn't ran,
     * the effects of the spell don't get removed (For instance, if a spell places a wall, the wall would not get removed because the server shut down while the task still needed to run)
     */
    public static void shutdown(){
        for(Wrapper wrapper : wrappers.values()){
            if(wrapper instanceof GrimmoireWrapper){
                GrimmoireWrapper gWrapper = (GrimmoireWrapper) wrapper;
                gWrapper.shutdown();
            }
        }
    }

}
