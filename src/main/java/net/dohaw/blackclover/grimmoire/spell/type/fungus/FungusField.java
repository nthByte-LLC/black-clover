package net.dohaw.blackclover.grimmoire.spell.type.fungus;

import net.dohaw.blackclover.config.GrimmoireConfig;
import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.PersistableSpell;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.PlayerData;
import net.dohaw.blackclover.util.BlockSnapshot;
import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FungusField extends CastSpellWrapper implements Listener, PersistableSpell {

    private double poisonDuration;
    private int poisonLevel;
    private double duration;

    private final String FUNGUS_FIELD_META_MARKER = "fungus-field-owner";
    private List<List<BlockSnapshot>> allFungusFieldBlocks = new ArrayList<>();

    public FungusField(GrimmoireConfig grimmoireConfig) {
        super(SpellType.FUNGUS_FIELD, grimmoireConfig);
    }

    @Override
    public boolean cast(Event e, PlayerData pd) {

        Player player = pd.getPlayer();
        Location locationUnderPlayer = player.getLocation().subtract(0, 1, 0);
        Material underPlayerMaterial = locationUnderPlayer.getBlock().getType();
        boolean isOnGround = underPlayerMaterial.isSolid();
        if(isOnGround){

            List<BlockSnapshot> spellFungusFieldBlocks = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    Block block = locationUnderPlayer.clone().add(i, 0, j).getBlock();
                    if(block.getType() == Material.GRASS_BLOCK || block.getType() == Material.DIRT){
                        spellFungusFieldBlocks.add(BlockSnapshot.toSnapshot(block));
                        block.setType(Material.MYCELIUM, false);
                        block.setMetadata(FUNGUS_FIELD_META_MARKER, new FixedMetadataValue(Grimmoire.instance, player.getName()));
                    }
                }
            }
            allFungusFieldBlocks.add(spellFungusFieldBlocks);

            // Poisons people who are standing in the fungus field
            BukkitTask fungusFieldChecker = Bukkit.getScheduler().runTaskTimer(Grimmoire.instance, () -> {

                for(BlockSnapshot snapshot : spellFungusFieldBlocks){

                    Location locationFungusBlock = snapshot.getLocation().clone();
                    Location topOfFungusBlock = locationFungusBlock.add(0, 1, 0);
                    SpellUtils.spawnParticle(topOfFungusBlock, Particle.TOTEM, 10, 0.1f, 0.1f , 0.1f);

                    Collection<Entity> entitiesOnTop = locationFungusBlock.getWorld().getNearbyEntities(topOfFungusBlock, 0.5f, 0.5f , 0.5f);

                    for(Entity en : entitiesOnTop){
                        if(en instanceof LivingEntity && !en.getUniqueId().equals(player.getUniqueId())){
                            LivingEntity le = (LivingEntity) en;
                            le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int) (poisonDuration * 20), poisonLevel - 1));
                        }
                    }

                }

            }, 0L, 20L);

            Bukkit.getScheduler().runTaskLater(Grimmoire.instance, () -> {
                fungusFieldChecker.cancel();
                removeFungusField(spellFungusFieldBlocks);
            }, (long) (duration * 20L));

            return true;

        }else{
            player.sendMessage("You can only use this spell when you are on the ground!");
        }

        return false;
    }

    @Override
    public void loadSettings() {
        super.loadSettings();
        this.duration = grimmoireConfig.getDoubleSetting(KEY, "Duration");
        this.poisonDuration = grimmoireConfig.getDoubleSetting(KEY, "Poison Duration");
        this.poisonLevel = grimmoireConfig.getIntegerSetting(KEY, "Poison Level");
    }

    @Override
    public void prepareShutdown() {
        for(List<BlockSnapshot> snapshots : allFungusFieldBlocks){
            removeFungusField(snapshots);
        }
    }

    private void removeFungusField(List<BlockSnapshot> snapshots){
        snapshots.forEach(snapshot -> {
            snapshot.apply();
            SpellUtils.spawnParticle(snapshot.getLocation(), Particle.SQUID_INK, 30, 1, 1, 1);
        });
    }

}
