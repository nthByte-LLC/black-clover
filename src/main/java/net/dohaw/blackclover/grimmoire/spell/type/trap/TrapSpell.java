package net.dohaw.blackclover.grimmoire.spell.type.trap;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.util.BlockSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class TrapSpell extends CastSpellWrapper {

    private Map<UUID, BlockSnapshot> carpetLocationSnapshots = new HashMap<>();

    public TrapSpell(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }


    @Override
    public void prepareShutdown() {
        for(BlockSnapshot loc : carpetLocationSnapshots.values()){
            loc.apply();
        }
    }
}
