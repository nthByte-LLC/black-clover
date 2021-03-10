package net.dohaw.blackclover.exception;

public class UnexpectedPlayerData extends Exception{

    public UnexpectedPlayerData(){
        super("Unexpected player data found! This only happens in rare scenarios!");
    }

}
