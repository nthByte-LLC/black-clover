package net.dohaw.blackclover.exception;

/**
 * For spells that need to manipulate custom player data. If the player doesn't have the expected player data, then that's a huge problem and the spell can't do what it needs to do.
 */
public class UnexpectedPlayerData extends Exception{

    public UnexpectedPlayerData(){
        super("Unexpected player data found! This only happens in rare scenarios!");
    }

}
