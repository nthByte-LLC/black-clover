package net.dohaw.blackclover.playerdata;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class TransformationPlayerData extends PlayerData{

    // The entity you are morphed into
    private LivingEntity morphedEntity;

    public TransformationPlayerData(UUID uuid) {
        super(uuid, Grimmoire.TRANSFORMATION);
    }

    public boolean isMorphed(){
        return morphedEntity != null;
    }

    public LivingEntity getMorphedEntity() {
        return morphedEntity;
    }

    public void setMorphedEntity(LivingEntity morphedEntity) {
        this.morphedEntity = morphedEntity;
    }

    @Override
    public void prepareDataRemoval() {
        super.prepareDataRemoval();
        if(isMorphed()){
            morphedEntity.remove();
        }
    }

    public void removeMorphedEntity(){
        morphedEntity.remove();
        morphedEntity = null;
    }

}
