package net.dohaw.blackclover.grimmoire.spell.type.ash;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BukkitColor;
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
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.List;

public class SmokeBomb extends CastSpellWrapper {

    private int radiusField;
    private int duration;

    public SmokeBomb(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SMOKE_BOMB, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();

        // makes the player unattackable but they also can't attack others
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration * 20, 0));
        pd.setCanCast(false);
        pd.setCanAttack(false);
        pd.setInVulnerable(true);

        final Location CAST_LOCATION = player.getLocation().clone();
        List<Block> particleLocations = ShapeUtils.makeCube(CAST_LOCATION, radiusField, Material.AIR);

        // Draws the smoke field
        BukkitTask particleDrawer = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            for(Block block : particleLocations){
                Location particleLocation = block.getLocation();
                SpellUtils.spawnParticle(particleLocation, Particle.REDSTONE, new Particle.DustOptions(BukkitColor.DARK_GREY, 1), 30, 0.5f, 0.5f, 0.5f);
            }
        }, 0, 20);

        BukkitTask playerBlinder = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {
            Collection<Entity> entitiesInField = CAST_LOCATION.getWorld().getNearbyEntities(CAST_LOCATION, radiusField, radiusField, radiusField);
            entitiesInField.removeIf(entity -> player.getUniqueId().equals(entity.getUniqueId()));
            for(Entity en : entitiesInField){
                if(en instanceof LivingEntity){
                    LivingEntity le = (LivingEntity) en;
                    le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5, 0));
                }
            }
        }, 0, 5);

        Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
            particleDrawer.cancel();
            playerBlinder.cancel();
            pd.setCanAttack(true);
            pd.setCanCast(true);
            pd.setInVulnerable(false);
        }, duration * 20);

        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getIntegerSetting(KEY, "Duration");
        this.radiusField = grimmoireConfig.getIntegerSetting(KEY, "Radius Field");
    }

}
