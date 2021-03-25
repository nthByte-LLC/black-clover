package net.dohaw.blackclover.grimmoire.spell.type.snow;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.event.SpellDamageEvent;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Snowball extends CastSpellWrapper implements Listener {

    private int damage;

    public Snowball(GrimmoireConfig grimmoireConfig) {
        super(SpellType.SNOW_BALL, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {
        SpellUtils.fireProjectile(pd.getPlayer(), this, Material.SNOWBALL);
        return true;
    }

    @EventHandler
    public void onProjectileDamage(SpellDamageEvent e){
        if(e.getSpell() == KEY){
            if(e.isCancelled()){
                e.setDamage(damage);
            }
        }
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.damage = grimmoireConfig.getIntegerSetting(KEY, "Damage");
    }
}
