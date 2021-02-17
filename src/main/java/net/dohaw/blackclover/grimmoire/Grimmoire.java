package net.dohaw.blackclover.grimmoire;

import net.dohaw.blackclover.Wrapper;
import net.dohaw.blackclover.WrapperHolder;
import net.dohaw.blackclover.grimmoire.type.Anti;
import net.dohaw.blackclover.grimmoire.type.Fire;
import net.dohaw.blackclover.grimmoire.type.Sand;

import java.util.ArrayList;
import java.util.List;

public class Grimmoire extends WrapperHolder {

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

}
