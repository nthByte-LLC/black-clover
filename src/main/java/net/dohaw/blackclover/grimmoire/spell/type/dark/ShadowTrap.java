package net.dohaw.blackclover.grimmoire.spell.type.dark;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.particle.EntityLineParticleRunner;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ShadowTrap extends ActivatableSpellWrapper {

    private double stunDuration;
    private int castDistance;

    public ShadowTrap(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SHADOW_TRAP, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player player = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
        if(SpellUtils.isTargetValid(player, entityInSight)){

            // Draws the shadow particles
            EntityLineParticleRunner shadowParticleDrawer = new EntityLineParticleRunner(player, entityInSight, new Particle.DustOptions(Color.BLACK, 2), 0.2);
            shadowParticleDrawer.setyOffset(-1);

            pd.addSpellRunnables(KEY, shadowParticleDrawer.runTaskTimer(Grimmoire.instance, 0L, 5L));
            return super.cast(e, pd);

        }
        return false;

    }

    @Override
    public void doRunnableTick(PlayerData caster) {

        Player player = caster.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(null, player, castDistance);
        if(SpellUtils.isTargetValid(player, entityInSight)){
            LivingEntity target = (LivingEntity) entityInSight;
            SpellUtils.freezeEntity(target, stunDuration);
        }else{
            deactiveSpell(caster);
        }

    }

    @Override
    public void deactiveSpell(PlayerData caster) {
        caster.stopSpellRunnables(KEY);
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.stunDuration = grimmoireConfig.getDoubleSetting(KEY, "Stun Duration");
    }

}
