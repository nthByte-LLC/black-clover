package net.dohaw.blackclover.grimmoire.spell.type.dark;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.PersistableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BlockSnapshot;
import net.dohaw.blackclover.util.ShapeUtils;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class DarkHeart extends CastSpellWrapper implements PersistableSpell {

    private List<BlockSnapshot> allPreviousBlocks = new ArrayList<>();

    private int witherLevel;
    private double witherDuration;
    private int radius;
    private double damage;
    private double duration;

    public DarkHeart(GrimmoireConfig grimmoireConfig) {
        super(SpellType.DARK_HEART, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        List<BlockSnapshot> previousBlocks = new ArrayList<>();

        Player caster = pd.getPlayer();
        List<Location> sphereBlocks = ShapeUtils.generateSphere(caster.getLocation(), radius, false);
        for(Location location : sphereBlocks){

            /*
                Turns all blocks within a certain radius sphere to black concrete blocks
             */
            Block locationBlock = location.getBlock();
            Material locationBlockType = locationBlock.getType();
            if(locationBlockType.isSolid()){
                previousBlocks.add(BlockSnapshot.toSnapshot(locationBlock));
                locationBlock.setType(Material.BLACK_CONCRETE);
            }

        }

        /*
            Does damage to all entities within the area and gives the wither effect.
         */
        for(Entity entity : caster.getNearbyEntities(radius, radius, radius)){

            if(entity instanceof LivingEntity){

                LivingEntity livingEntity = (LivingEntity) entity;
                boolean isDamageDone = SpellUtils.doSpellDamage(livingEntity, caster, KEY, damage);

                if(isDamageDone){
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (int) (witherDuration * 20), witherLevel - 1));
                }

            }

        }

        SpellUtils.playSound(caster, Sound.ENTITY_WITHER_HURT);
        SpellUtils.spawnParticle(caster.getLocation(), Particle.REDSTONE, new Particle.DustOptions(Color.BLACK, 2), 30, 1, 1, 1);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            previousBlocks.forEach(BlockSnapshot::apply);
            allPreviousBlocks.removeAll(previousBlocks);
        }, (long) (duration * 20L));

        return true;

    }

    @Override
    public void prepareShutdown() {
        allPreviousBlocks.forEach(BlockSnapshot::apply);
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.radius = grimmoireConfig.getIntegerSetting(KEY, "Radius");
        this.witherLevel = grimmoireConfig.getIntegerSetting(KEY, "Wither Level");
        this.witherDuration = grimmoireConfig.getDoubleSetting(KEY, "Wither Duration");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
    }

}
