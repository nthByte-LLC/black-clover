package net.dohaw.blackclover.grimmoire.spell.type.glass;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.PersistableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class GlassSnake extends CastSpellWrapper implements PersistableSpell {

    private List<List<Location>> snakeBlocks = new ArrayList<>();

    private double damage;
    private int lengthSnake, castDistance;

    public GlassSnake(GrimmoireConfig grimmoireConfig) {
        super(SpellType.GLASS_SNAKE, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        Player caster = pd.getPlayer();
        Entity entityInSight = SpellUtils.getEntityInLineOfSight(e, caster, castDistance);
        if(SpellUtils.isTargetValid(caster, entityInSight)){

            List<Location> currentSnakeBlocks = new ArrayList<>();
            for(int i = 0; i < lengthSnake; i++){

                Location entityLocation = entityInSight.getLocation();
                Location snakeBlockLocation = LocationUtil.getLocationInDirection(caster.getLocation(), entityInSight.getLocation(), i);
                double distanceFromEnemy = snakeBlockLocation.distance(entityLocation);

                if(distanceFromEnemy < 1){
                    break;
                }

                Block snakeBlock = snakeBlockLocation.getBlock();
                if(snakeBlock.getType() == Material.AIR){
                    snakeBlock.setType(Material.GLASS);
                    currentSnakeBlocks.add(snakeBlockLocation);
                }

            }
            snakeBlocks.add(currentSnakeBlocks);

            Location firstSnakeBlockLocation = currentSnakeBlocks.get(0);
            boolean damageHasBeenDone = SpellUtils.doSpellDamage((LivingEntity) entityInSight, caster, KEY, damage);
            if(damageHasBeenDone){
                SpellUtils.playSound(firstSnakeBlockLocation, Sound.BLOCK_GLASS_PLACE);
                SpellUtils.spawnParticle(firstSnakeBlockLocation, Particle.CLOUD, 30, 1, 1, 1);
            }

            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                snakeBlocks.remove(currentSnakeBlocks);
                removeSnakeBlocks(currentSnakeBlocks);
                SpellUtils.playSound(firstSnakeBlockLocation, Sound.BLOCK_GLASS_BREAK);
            }, 10L);

            return true;

        }

        return false;
    }

    @Override
    public void prepareShutdown() {
        for (List<Location> snake : snakeBlocks) {
            removeSnakeBlocks(snake);
        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.lengthSnake = grimmoireConfig.getIntegerSetting(KEY, "Length Snake");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.damage = grimmoireConfig.getDoubleSetting(KEY, "Damage");
    }

    private void removeSnakeBlocks(List<Location> snakeBlocks){
        snakeBlocks.forEach(loc -> loc.getBlock().setType(Material.AIR));
    }

}
