package net.dohaw.blackclover.grimmoire.spell.type.shakudo;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.ShakudoPlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;

import java.util.List;

public class Goodie extends CastSpellWrapper {

    private double addedHealth;

    public Goodie(GrimmoireConfig grimmoireConfig) {
        super(SpellType.GOODIE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        ShakudoPlayerData spd = (ShakudoPlayerData) pd;
        if(hasWolvesOut(spd)){
            if(spd.isPackCalled()){
                List<Wolf> pack = spd.getPack();
                pack.forEach(this::giveHealth);
            }else{
                Wolf wolf = spd.getWolf();
                giveHealth(wolf);
            }
        }
        return true;
    }

    private boolean hasWolvesOut(ShakudoPlayerData spd){
        return spd.isPackCalled() || spd.isSingularWolfSpawned();
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.addedHealth = grimmoireConfig.getDoubleSetting(KEY, "Added Health");
    }

    private void giveHealth(Wolf wolf){
        double wolfHealth = wolf.getHealth();
        double newHealth = wolfHealth + addedHealth;
        if(newHealth <= 20){
            wolf.setHealth(newHealth);
        }
        SpellUtils.spawnParticle(wolf, Particle.VILLAGER_HAPPY, 10, 1f, 1f, 1f);
        SpellUtils.playSound(wolf, Sound.ENTITY_GENERIC_EAT);
    }

}
