package net.dohaw.blackclover.grimmoire.spell.type.transformation;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.menu.transformation.HostileMobsMenu;
import net.dohaw.blackclover.menu.transformation.PassiveMobsMenu;
import net.dohaw.blackclover.menu.transformation.TransformationMenu;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.playerdata.TransformationPlayerData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MorphSpell<T extends TransformationMenu> extends CastSpellWrapper implements Listener {

    private MorphMenuType morphMenuType;

    public MorphSpell(SpellType spellType, GrimmoireConfig grimmoireConfig, MorphMenuType morphMenuType) {
        super(spellType, grimmoireConfig);
        this.morphMenuType = morphMenuType;
    }

    @Override
    public boolean cast(Event e, PlayerData pd) throws UnexpectedPlayerData {

        if(pd instanceof TransformationPlayerData){

            Player player = pd.getPlayer();
            TransformationPlayerData tpd = (TransformationPlayerData) pd;
            if(!tpd.isMorphed()){

                T menu = getMenu();
                if(menu != null){
                    menu.initializeItems(player);
                    menu.openInventory(player);
                }

            }else{
                if(player.isSneaking()){
                    stopMorphing(tpd);
                }else{
                    player.sendMessage("You are already morphed!");
                }
            }

        }else{
            throw new UnexpectedPlayerData();
        }

        return false;
    }

    private T getMenu(){
        switch(morphMenuType){
            case PASSIVE_MOB:
                return (T) new PassiveMobsMenu(Grimmoire.instance);
            case HOSTILE_MOB:
                return (T) new HostileMobsMenu(Grimmoire.instance);
            default:
                return null;
        }
    }

    private void stopMorphing(TransformationPlayerData tpd){
        tpd.removeMorphedEntity();
        tpd.getPlayer().setInvisible(false);
    }

    /*
        Teleports the entity to the player if they are currently morphed.
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        PlayerData pd = Grimmoire.instance.getPlayerDataManager().getData(player);
        if(pd instanceof TransformationPlayerData){
            TransformationPlayerData tpd = (TransformationPlayerData) pd;
            if(tpd.isMorphed()){
                LivingEntity morphedEntity = tpd.getMorphedEntity();
                /*
                    Checks to see if the entity is dead. If so, then it stops the morphing
                 */
                if(morphedEntity.isValid()){
                    tpd.getMorphedEntity().teleport(player.getLocation());
                }else{
                    stopMorphing(tpd);
                }
            }
        }
    }

    @Override
    public void prepareShutdown() { }

}
