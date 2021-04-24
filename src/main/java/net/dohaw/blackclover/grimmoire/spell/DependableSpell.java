package net.dohaw.blackclover.grimmoire.spell;

/**
 * A spell that depends on another spell and needs to do specific things after the registration of the grimmoires
 */
public interface DependableSpell {
    void initDependableData();
}
