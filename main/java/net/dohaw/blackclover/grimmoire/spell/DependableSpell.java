package net.dohaw.blackclover.grimmoire.spell;

/**
 * A spell that depends on some sort of functionality. It can depend on another spell or some sort of functionality.
 * Usually implement this if you want to do something when the spell class initializes.
 */
public interface DependableSpell {
    void initDependableData();
}
