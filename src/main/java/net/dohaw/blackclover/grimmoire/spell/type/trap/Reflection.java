package net.dohaw.blackclover.grimmoire.spell.type.trap;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.TrapPlayerData;
import net.dohaw.blackclover.runnable.particle.CircleParticleRunner;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class Reflection extends TrapSpell{

    // How long the player can reflect spells
    private double reflectionDuration;

    public Reflection(SpellType spellType, GrimmoireConfig grimmoireConfig) {
        super(spellType, grimmoireConfig);
    }

    @Override
    public void onStepOnTrap(Trap trap, LivingEntity trapStepper) {

        UUID trapStepperUUID = trapStepper.getUniqueId();
        if(trap.getOwner().equals(trapStepperUUID)){

            TrapPlayerData tpd = (TrapPlayerData) Grimmoire.instance.getPlayerDataManager().getData(trapStepperUUID);
            tpd.setReflectingSpells(true);
            SpellUtils.spawnParticle(trapStepper, Particle.CLOUD, 30, 0.5f, 0.5f, 0.5f);
            SpellUtils.playSound(trapStepper, Sound.ITEM_SHIELD_BLOCK);

            CircleParticleRunner particleRunner = new CircleParticleRunner(trapStepper, new Particle.DustOptions(BukkitColor.CYAN, 1), true, 1);
            particleRunner.setMaxYAdditive(0.5);

            tpd.addSpellRunnables(KEY, particleRunner.runTaskTimer(Grimmoire.instance, 0L, 3L));
            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
               tpd.setReflectingSpells(false);
               tpd.stopSpellRunnables(KEY);
            }, (long) (reflectionDuration * 20L));

        }

    }

    @EventHandler
    public void onTakeSpellDamage(SpellDamageEvent e){

        Entity eDamaged = e.getDamaged();
        if(eDamaged instanceof Player){

            Player damaged = (Player) eDamaged;
            PlayerData damagedPlayerData = Grimmoire.instance.getPlayerDataManager().getData(damaged);
            if(damagedPlayerData.getGrimmoireType() == GrimmoireType.TRAP){

                TrapPlayerData tpd = (TrapPlayerData) damagedPlayerData;
                if(tpd.isReflectingSpells()){
                    Player damager = e.getDamager();
                    SpellUtils.doSpellDamage(damager, damaged, KEY, e.getDamage());
                    tpd.setReflectingSpells(false);
                    e.setCancelled(true);
                }

            }

        }

    }

    @Override
    public Material getCarpetMaterial() {
        return Material.LIGHT_BLUE_CARPET;
    }

    @Override
    public Particle placeParticles() {
        return Particle.SPELL_INSTANT;
    }

    @Override
    public TrapType getTrapType() {
        return TrapType.REFLECTION;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.reflectionDuration = grimmoireConfig.getDoubleSetting(KEY, "Reflection Duration");
    }

}
