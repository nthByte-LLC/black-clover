package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transformation extends GrimmoireWrapper {

    private final Map<EntityType, Material> PASSIVE_MOB_EGGS = new HashMap<>();
    private final Map<EntityType, Material> HOSTILE_MOB_EGGS = new HashMap<>();

    public Transformation() {
        super(GrimmoireType.TRANSFORMATION);
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public int getTier() {
        return 0;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return null;
    }

    @Override
    public void initSpells() {

    }

    private void compilePassiveMobsEggs(){
        PASSIVE_MOB_EGGS.put(EntityType.BAT, Material.BAT_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.CAT, Material.CAT_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.CHICKEN, Material.CHICKEN_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.COW, Material.COW_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.DONKEY, Material.DONKEY_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.FOX, Material.FOX_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.HORSE, Material.HORSE_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.MUSHROOM_COW, Material.COW_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.MULE, Material.MULE_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.OCELOT, Material.OCELOT_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.PARROT, Material.PARROT_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.PIG, Material.PIG_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.POLAR_BEAR, Material.POLAR_BEAR_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.RABBIT, Material.RABBIT_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.SHEEP, Material.SHEEP_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.SKELETON_HORSE, Material.SKELETON_HORSE_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.SNOWMAN, Material.PUMPKIN);
        PASSIVE_MOB_EGGS.put(EntityType.SQUID, Material.SQUID_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.TURTLE, Material.TURTLE_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.VILLAGER, Material.VILLAGER_SPAWN_EGG);
        PASSIVE_MOB_EGGS.put(EntityType.WANDERING_TRADER, Material.WANDERING_TRADER_SPAWN_EGG);
    }

    private void compileHostileMobsEggs(){
        HOSTILE_MOB_EGGS.put(EntityType.BLAZE, Material.BLAZE_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.CREEPER, Material.CREEPER_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.DROWNED, Material.DROWNED_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.ELDER_GUARDIAN, Material.ELDER_GUARDIAN_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.ENDERMITE, Material.ENDERMITE_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.EVOKER, Material.EVOKER_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.GHAST, Material.GHAST_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.GUARDIAN, Material.GUARDIAN_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.HOGLIN, Material.HOGLIN_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.HUSK, Material.HUSK_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.MAGMA_CUBE, Material.MAGMA_CUBE_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.PHANTOM, Material.PHANTOM_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.PIGLIN_BRUTE, Material.PIGLIN_BRUTE_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.PILLAGER, Material.PILLAGER_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.RAVAGER, Material.RAVAGER_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.SHULKER, Material.BLAZE_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.BLAZE, Material.BLAZE_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.BLAZE, Material.BLAZE_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.BLAZE, Material.BLAZE_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.BLAZE, Material.BLAZE_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.BLAZE, Material.BLAZE_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.BLAZE, Material.BLAZE_SPAWN_EGG);
        HOSTILE_MOB_EGGS.put(EntityType.BLAZE, Material.BLAZE_SPAWN_EGG);
    }

}
