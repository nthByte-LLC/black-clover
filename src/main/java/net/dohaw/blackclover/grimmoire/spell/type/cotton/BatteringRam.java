package net.dohaw.blackclover.grimmoire.spell.type.cotton;

import lombok.Getter;
import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.CottonPlayerData;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.runnable.spells.BatteringRamGoalChecker;
import net.dohaw.blackclover.util.EntityUtil;
import net.dohaw.blackclover.util.LocationUtil;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Event;

public class BatteringRam extends CastSpellWrapper {

    @Getter
    private double damageDistance;

    @Getter
    private int damage;

    private int castDistance;

    public BatteringRam(GrimmoireConfig grimmoireConfig) {
        super(SpellType.BATTERING_RAM, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        if(pd instanceof CottonPlayerData){

            Player player = pd.getPlayer();
            CottonPlayerData cpd = (CottonPlayerData) pd;

            if(!cpd.isGoldenSheepSpawned()){

                Entity entityInSight = SpellUtils.getEntityInLineOfSight(player, castDistance);
                if(SpellUtils.isTargetValid(player, entityInSight)){

                    Location sheepSpawnLocation = LocationUtil.getLocationInFront(player, 1).add(0, 1, 0);
                    Sheep goldenSheep = (Sheep) player.getWorld().spawnEntity(sheepSpawnLocation, EntityType.SHEEP);
                    goldenSheep.setColor(DyeColor.YELLOW);
                    goldenSheep.setGlowing(true);
                    cpd.setGoldenSheep(goldenSheep);
                    EntityUtil.makeEntityFollow(goldenSheep, (LivingEntity) entityInSight);

                    SpellUtils.playSound(goldenSheep, Sound.ENTITY_GENERIC_EXPLODE);
                    SpellUtils.spawnParticle(goldenSheep, Particle.END_ROD, 10, 1, 1, 1);
                    SpellUtils.startTornadoParticles(goldenSheep, new Particle.DustOptions(Color.YELLOW, 1), true, 1, true);
                    new BatteringRamGoalChecker(goldenSheep, (LivingEntity) entityInSight, cpd, this).runTaskTimer(Grimmoire.instance, 0L, 5L);
                    return true;

                }

            }else{
                if(player.isSneaking()){
                    Sheep goldenSheep = cpd.getGoldenSheep();
                    SpellUtils.spawnParticle(goldenSheep, Particle.SQUID_INK, 10, 1, 1, 1);
                    SpellUtils.playSound(goldenSheep, Sound.ENTITY_SQUID_SQUIRT);
                    cpd.removeGoldenSheep();
                }else{
                    player.sendMessage("You already have spawned the golden sheep!");
                }
            }



        }else{
            throw new UnexpectedPlayerData();
        }

        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
        this.damage = grimmoireConfig.getIntegerSetting(KEY, "Damage");
        this.damageDistance = grimmoireConfig.getDoubleSetting(KEY, "Damage Distance");
    }
}
