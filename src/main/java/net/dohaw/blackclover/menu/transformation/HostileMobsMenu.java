package net.dohaw.blackclover.menu.transformation;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class HostileMobsMenu extends TransformationMobMenu {

    public HostileMobsMenu(JavaPlugin plugin) {
        super(plugin, null, "Hostile Mobs Transformation", 36);
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {
        super.onInventoryClick(e);
    }

    @Override
    public CastSpellWrapper getTransformationSpell() {
        return Grimmoire.TRANSFORMATION.monstopher;
    }

    @Override
    public void compileMobEggs() {
        MOB_EGGS.put(EntityType.BLAZE, Material.BLAZE_SPAWN_EGG);
        MOB_EGGS.put(EntityType.CREEPER, Material.CREEPER_SPAWN_EGG);
        MOB_EGGS.put(EntityType.DROWNED, Material.DROWNED_SPAWN_EGG);
        MOB_EGGS.put(EntityType.ELDER_GUARDIAN, Material.ELDER_GUARDIAN_SPAWN_EGG);
        MOB_EGGS.put(EntityType.ENDERMITE, Material.ENDERMITE_SPAWN_EGG);
        MOB_EGGS.put(EntityType.EVOKER, Material.EVOKER_SPAWN_EGG);
        MOB_EGGS.put(EntityType.GHAST, Material.GHAST_SPAWN_EGG);
        MOB_EGGS.put(EntityType.GUARDIAN, Material.GUARDIAN_SPAWN_EGG);
        MOB_EGGS.put(EntityType.HOGLIN, Material.HOGLIN_SPAWN_EGG);
        MOB_EGGS.put(EntityType.HUSK, Material.HUSK_SPAWN_EGG);
        MOB_EGGS.put(EntityType.MAGMA_CUBE, Material.MAGMA_CUBE_SPAWN_EGG);
        MOB_EGGS.put(EntityType.PHANTOM, Material.PHANTOM_SPAWN_EGG);
        MOB_EGGS.put(EntityType.PIGLIN_BRUTE, Material.PIGLIN_BRUTE_SPAWN_EGG);
        MOB_EGGS.put(EntityType.PILLAGER, Material.PILLAGER_SPAWN_EGG);
        MOB_EGGS.put(EntityType.RAVAGER, Material.RAVAGER_SPAWN_EGG);
        MOB_EGGS.put(EntityType.SHULKER, Material.SHULKER_SPAWN_EGG);
        MOB_EGGS.put(EntityType.SILVERFISH, Material.SILVERFISH_SPAWN_EGG);
        MOB_EGGS.put(EntityType.SKELETON, Material.SKELETON_SPAWN_EGG);
        MOB_EGGS.put(EntityType.SKELETON_HORSE, Material.SKELETON_HORSE_SPAWN_EGG);
        MOB_EGGS.put(EntityType.SLIME, Material.SLIME_SPAWN_EGG);
        MOB_EGGS.put(EntityType.STRAY, Material.STRAY_SPAWN_EGG);
        MOB_EGGS.put(EntityType.VEX, Material.VEX_SPAWN_EGG);
        MOB_EGGS.put(EntityType.VINDICATOR, Material.VINDICATOR_SPAWN_EGG);
        MOB_EGGS.put(EntityType.WITCH, Material.WITCH_SPAWN_EGG);
        MOB_EGGS.put(EntityType.WITHER_SKELETON, Material.WITHER_SKELETON_SPAWN_EGG);
        MOB_EGGS.put(EntityType.ZOGLIN, Material.ZOGLIN_SPAWN_EGG);
        MOB_EGGS.put(EntityType.ZOMBIE, Material.ZOMBIE_SPAWN_EGG);
        MOB_EGGS.put(EntityType.ZOMBIE_VILLAGER, Material.ZOMBIE_VILLAGER_SPAWN_EGG);
    }

}
