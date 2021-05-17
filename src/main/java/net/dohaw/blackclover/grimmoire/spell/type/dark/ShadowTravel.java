package net.dohaw.blackclover.grimmoire.spell.type.dark;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ShadowTravel extends ActivatableSpellWrapper {

    public ShadowTravel(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SHADOW_TRAVEL, grimmoireConfig);
    }

    @Override
    public void doRunnableTick(PlayerData caster) {

        Player casterPlayer = caster.getPlayer();
        SpellUtils.spawnParticle(casterPlayer.getLocation(), Particle.REDSTONE, new Particle.DustOptions(Color.BLACK, 2), 30, 1, 1, 1);

    }

    @Override
    public void deactiveSpell(PlayerData caster) throws UnexpectedPlayerData {

    }

    @Override
    public void prepareShutdown() {

    }

}
