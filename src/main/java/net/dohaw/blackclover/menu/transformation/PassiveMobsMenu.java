package net.dohaw.blackclover.menu.transformation;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PassiveMobsMenu extends TransformationMobMenu {

    public PassiveMobsMenu(JavaPlugin plugin) {
        super(plugin, null, "Passive Mobs Transformation", 27);
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {
        super.onInventoryClick(e);
    }

    @Override
    public CastSpellWrapper getTransformationSpell() {
        return Grimmoire.TRANSFORMATION.animorpher;
    }

    @Override
    public void compileMobEggs() {
        MOB_EGGS.put(EntityType.BAT, Material.BAT_SPAWN_EGG);
        MOB_EGGS.put(EntityType.CAT, Material.CAT_SPAWN_EGG);
        MOB_EGGS.put(EntityType.CHICKEN, Material.CHICKEN_SPAWN_EGG);
        MOB_EGGS.put(EntityType.COW, Material.COW_SPAWN_EGG);
        MOB_EGGS.put(EntityType.DONKEY, Material.DONKEY_SPAWN_EGG);
        MOB_EGGS.put(EntityType.FOX, Material.FOX_SPAWN_EGG);
        MOB_EGGS.put(EntityType.HORSE, Material.HORSE_SPAWN_EGG);
        MOB_EGGS.put(EntityType.MUSHROOM_COW, Material.COW_SPAWN_EGG);
        MOB_EGGS.put(EntityType.MULE, Material.MULE_SPAWN_EGG);
        MOB_EGGS.put(EntityType.OCELOT, Material.OCELOT_SPAWN_EGG);
        MOB_EGGS.put(EntityType.PARROT, Material.PARROT_SPAWN_EGG);
        MOB_EGGS.put(EntityType.PIG, Material.PIG_SPAWN_EGG);
        MOB_EGGS.put(EntityType.POLAR_BEAR, Material.POLAR_BEAR_SPAWN_EGG);
        MOB_EGGS.put(EntityType.RABBIT, Material.RABBIT_SPAWN_EGG);
        MOB_EGGS.put(EntityType.SHEEP, Material.SHEEP_SPAWN_EGG);
        MOB_EGGS.put(EntityType.SKELETON_HORSE, Material.SKELETON_HORSE_SPAWN_EGG);
        MOB_EGGS.put(EntityType.SNOWMAN, Material.PUMPKIN);
        MOB_EGGS.put(EntityType.SQUID, Material.SQUID_SPAWN_EGG);
        MOB_EGGS.put(EntityType.TURTLE, Material.TURTLE_EGG);
        MOB_EGGS.put(EntityType.VILLAGER, Material.VILLAGER_SPAWN_EGG);
        MOB_EGGS.put(EntityType.WANDERING_TRADER, Material.WANDERING_TRADER_SPAWN_EGG);
    }

}
