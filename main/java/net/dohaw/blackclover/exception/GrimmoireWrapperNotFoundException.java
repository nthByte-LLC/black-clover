package net.dohaw.blackclover.exception;

public class GrimmoireWrapperNotFoundException extends Exception{

    public GrimmoireWrapperNotFoundException(){
        super("The grimmoire wrapper that you tried to find is null!");
    }

}
