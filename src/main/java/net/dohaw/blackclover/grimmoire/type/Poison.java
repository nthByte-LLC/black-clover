package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.poison.Antidote;
import net.dohaw.blackclover.grimmoire.spell.type.poison.BadBeath;
import net.dohaw.blackclover.grimmoire.spell.type.poison.Plague;
import net.dohaw.blackclover.grimmoire.spell.type.poison.Shock;
import net.dohaw.blackclover.util.BukkitColor;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;

public class Poison extends GrimmoireWrapper {

    public Plague plague;
    public Antidote antidote;
    public net.dohaw.blackclover.grimmoire.spell.type.poison.Poison poison;
    public BadBeath badBeath;
    public Shock shock;

    public Poison() {
        super(GrimmoireType.POISON);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("poison");
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DAMAGE_HEAL;
    }

    @Override
    public void initSpells() {

        this.shock = new Shock(config);
        this.spells.put(SpellType.SHOCK, shock);

        this.badBeath = new BadBeath(config);
        this.spells.put(SpellType.BAD_BREATH, badBeath);

        this.poison = new net.dohaw.blackclover.grimmoire.spell.type.poison.Poison(config);
        this.spells.put(SpellType.POISON, poison);

        this.antidote = new Antidote(config);
        this.spells.put(SpellType.ANTIDOTE, antidote);

        this.plague = new Plague(config);
        this.spells.put(SpellType.PLAGUE, plague);

    }

    /**
     * Spawns the poison particles for an entity and plays a sound.
     * @param target The target that is getting the poison
     * @param duration How long the poison goes on for
     */
    public void startPoisonEffect(LivingEntity target, double duration){

        SpellUtils.playSound(target, Sound.BLOCK_CROP_BREAK);
        Particle.DustOptions dustOptions = new Particle.DustOptions(BukkitColor.CYAN, 1.5f);
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            if(target.hasPotionEffect(PotionEffectType.POISON)){
                SpellUtils.spawnParticle(target.getLocation(), Particle.REDSTONE, dustOptions, 30, 1, 1, 1);
            }
        }, 20L, 20L);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, task::cancel, (long) (duration * 20));

    }

}
