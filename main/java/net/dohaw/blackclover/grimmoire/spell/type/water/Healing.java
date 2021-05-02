package net.dohaw.blackclover.grimmoire.spell.type.water;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import net.dohaw.corelib.ResponderFactory;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Healing extends CastSpellWrapper {

    protected int castDistance;
    protected int targetHealAmount;
    protected int selfHealAmount;

    public Healing(GrimmoireConfig grimmoireConfig) {
        super(SpellType.HEALING, grimmoireConfig);
    }

    public Healing(SpellType spellType, GrimmoireConfig grimmoireConfig){
        super(spellType, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        Player player = pd.getPlayer();
        // self heal
        if(player.isSneaking()){
            heal(player, selfHealAmount);
        }else{
            Entity entityInLineOfSight = SpellUtils.getEntityInLineOfSight(e, player, castDistance);
            ResponderFactory rf = new ResponderFactory(player);
            if(entityInLineOfSight != null){
                if(entityInLineOfSight instanceof Player){
                    heal((Player) entityInLineOfSight, targetHealAmount);
                }else{
                    rf.sendMessage("This is not a valid player!");
                    return false;
                }
            }else{
                rf.sendMessage("There was no entity within a reasonable distance of you!");
                return false;
            }

        }
        return true;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.selfHealAmount = grimmoireConfig.getIntegerSetting(KEY, "Self Heal Amount");
        this.targetHealAmount = grimmoireConfig.getIntegerSetting(KEY, "Target Heal Amount");
        this.castDistance = grimmoireConfig.getIntegerSetting(KEY, "Cast Distance");
    }

    @Override
    public void prepareShutdown() {

    }

    private void heal(Player player, int additive){
        double playerHealth = player.getHealth();
        double newPlayerHealth = playerHealth + additive;
        if(newPlayerHealth > 20){
            newPlayerHealth = 20;
        }
        player.setHealth(newPlayerHealth);
        SpellUtils.playSound(player, Sound.ENTITY_VILLAGER_CELEBRATE);
        SpellUtils.spawnParticle(player, Particle.VILLAGER_HAPPY, 30, 0.5f, 0.5f, 0.5f);
    }

}
