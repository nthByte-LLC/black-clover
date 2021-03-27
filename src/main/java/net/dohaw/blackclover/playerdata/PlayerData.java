package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.config.PlayerDataConfig;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class PlayerData {

    /*
        For spells that have a cast time like "Freeze" from the Snow grimmoire
     */
    @Getter @Setter
    private boolean isCurrentlyCasting;

    @Getter @Setter
    private double castStartHealth;

    @Getter @Setter
    private SpellType spellCurrentlyCasting;

    @Setter
    private boolean canCast = true;

    @Getter @Setter
    private boolean isFrozen;

    @Getter @Setter
    private boolean isInVulnerable;

    @Getter
    private HashSet<SpellType> spellsOnCooldown = new HashSet<>();

    @Getter
    private Map<SpellType, List<BukkitTask>> spellRunnables = new HashMap<>();

    @Getter @Setter
    private int maxRegen, regenAmount;

    @Getter @Setter
    private GrimmoireWrapper grimmoireWrapper;

    @Getter @Setter
    private PlayerDataConfig config;

    @Getter
    private UUID uuid;

    /*
        For when you're loading the data
     */
    public PlayerData(UUID uuid, GrimmoireWrapper grimmoireWrapper) {
        this.uuid = uuid;
        this.grimmoireWrapper = grimmoireWrapper;
    }

    /*
        For when you're creating data and setting the grimmoire wrapper later.
     */
    public PlayerData(UUID uuid){
        this.uuid = uuid;
    }

    private void saveData(){
        config.saveData(this);
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public boolean hasSufficientRegenForSpell(CastSpellWrapper spell){
        return regenAmount >= spell.getRegenConsumed();
    }

    public boolean isSpellOnCooldown(SpellType spellType){
        return spellsOnCooldown.contains(spellType);
    }

    public boolean isSpellActive(SpellType spellType){
        return spellRunnables.containsKey(spellType);
    }

    public void stopSpellRunnables(SpellType spellType){
        List<BukkitTask> runnables = spellRunnables.get(spellType);
        if(runnables != null){
            runnables.forEach(BukkitTask::cancel);
            spellRunnables.remove(spellType);
        }
    }

    public void stopAllRunnables(){
        spellRunnables.forEach((k , l) -> {
            l.forEach(BukkitTask::cancel);
        });
        spellRunnables.clear();
    }

    public void addSpellRunnable(SpellType spellType, BukkitTask ...task){
        List<BukkitTask> tasks = new ArrayList<>();
        if(spellRunnables.containsKey(spellType)){
            tasks = spellRunnables.get(spellType);
        }
        tasks.addAll(Arrays.asList(task));
        spellRunnables.put(spellType, tasks);
    }

    public void stopTimedCast(){
        this.castStartHealth = 0;
        this.isCurrentlyCasting = false;
        if(spellRunnables.containsKey(spellCurrentlyCasting)){
            stopSpellRunnables(spellCurrentlyCasting);
        }
        this.spellCurrentlyCasting = null;
    }

    public void merge(PlayerData previousData){
        this.maxRegen = previousData.maxRegen;
        this.regenAmount = previousData.regenAmount;
        this.config = previousData.config;
    }

    public boolean canCast(){
        if(!(this instanceof AshPlayerData)){
            return canCast;
        }else{
            // players in ash form can't cast.
            AshPlayerData apd = (AshPlayerData) this;
            return canCast && !apd.isInAshForm();
        }
    }

    public void prepareDataRemoval(){
        saveData();
    }

}
