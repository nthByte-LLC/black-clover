package net.dohaw.blackclover.grimmoire.spell;

/**
 * Defines whether a spell has data that needs to be dealt with if the server prepareShutdown before the spell finishes whatever it's doing.
 */
public interface PersistableSpell {
    void prepareShutdown();
}
