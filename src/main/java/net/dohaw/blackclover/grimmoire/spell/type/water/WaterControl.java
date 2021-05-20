package net.dohaw.blackclover.grimmoire.spell.type.water;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WaterControl extends ActivatableSpellWrapper implements Listener {

    private int strengthLevel, resistanceLevel, checkingRadius;

    public WaterControl(GrimmoireConfig grimmoireConfig) {
        super(SpellType.WATER_CONTROL, grimmoireConfig);
    }

    @Override
    public void doRunnableTick(PlayerData caster) {
        Player player = caster.getPlayer();
        if(isNearWater(player)){
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,  21, resistanceLevel - 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 21, strengthLevel - 1));
            SpellUtils.playSound(player, Sound.ENTITY_BOAT_PADDLE_WATER);
            SpellUtils.spawnParticle(player, Particle.FALLING_WATER, 30, 1, 1, 1);
        }else{
            SpellUtils.spawnParticle(player, Particle.BLOCK_DUST, Material.SAND.createBlockData(), 30, 0.5f, 0.5f, 0.5f);
        }
    }

    @Override
    public void deactiveSpell(PlayerData caster) throws UnexpectedPlayerData {

    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.strengthLevel = grimmoireConfig.getIntegerSetting(KEY, "Strength Potion Level");
        this.resistanceLevel = grimmoireConfig.getIntegerSetting(KEY, "Resistance Potion Level");
        this.checkingRadius = grimmoireConfig.getIntegerSetting(KEY, "Water Checking Radius");
    }

    private boolean isNearWater(Player player){
        Location start = player.getLocation();
        World world = start.getWorld();
        assert world != null;
        for(int x = start.getBlockX() - checkingRadius; x <= start.getX() + checkingRadius; x++){
            for(int y = start.getBlockY() - checkingRadius; y <= start.getY() + checkingRadius; y++){
                for(int z = start.getBlockZ() - checkingRadius; z <= start.getZ() + checkingRadius; z++){
                    Block block = world.getBlockAt(x, y, z);
                    if(block.getType() == Material.WATER){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
