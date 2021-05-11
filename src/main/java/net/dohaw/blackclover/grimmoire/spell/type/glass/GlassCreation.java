package net.dohaw.blackclover.grimmoire.spell.type.glass;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import org.bukkit.entity.Player;

public class GlassCreation extends ActivatableSpellWrapper {

    public GlassCreation(GrimmoireConfig grimmoireConfig) {
        super(SpellType.GLASS_CREATION, grimmoireConfig);
    }

    @Override
    public void doRunnableTick(PlayerData caster) {

        Player player = caster.getPlayer();


    }

    @Override
    public void deactiveSpell(PlayerData caster) throws UnexpectedPlayerData {

    }

    @Override
    public void prepareShutdown() {

    }

    @Override
    public long getRunnableInterval() {
        return 10L;
    }

}
