package net.dohaw.blackclover.grimmoire.spell.type.fungus;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class FungusMeal extends CastSpellWrapper {

    public FungusMeal(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FUNGUS_MEAL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        player.setFoodLevel(20);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            SpellUtils.playSound(player, Sound.ENTITY_PLAYER_BURP);
        }, 3);
        SpellUtils.playSound(player, Sound.ENTITY_GENERIC_EAT);
        SpellUtils.spawnParticle(player, Particle.VILLAGER_HAPPY, 30, 1, 1 ,1);
        SpellUtils.spawnParticle(player, Particle.HEART, 10, 0.5f, 0.5f, 0.5f);

        return true;
    }

}
