package net.dohaw.blackclover;

import java.util.HashMap;
import java.util.Map;

public class WrapperHolder {

    public static Map<Enum, Wrapper> wrappers = new HashMap<>();

    public static Wrapper getByKey(Enum key){
        return wrappers.get(key);
    }

    public static void registerWrapper(Wrapper wrapper){
        if(wrappers.containsKey(wrapper.getKEY())){
            throw new IllegalArgumentException("This wrapper (" + wrapper.KEY + ") is already registered!");
        }
        wrappers.put(wrapper.getKEY(), wrapper);
    }

}
