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

    @Getter @Setter
    private boolean canCast = true;

    @Getter
    private HashSet<SpellType> spellsOnCooldown = new HashSet<>();

    @Getter
    private Map<SpellType, List<BukkitTask>> activeSpells = new HashMap<>();

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
        return activeSpells.containsKey(spellType);
    }

    public void removeActiveSpell(SpellType spellType){
        List<BukkitTask> runnables = activeSpells.get(spellType);
        runnables.forEach(BukkitTask::cancel);
        activeSpells.remove(spellType);
    }

    public void addActiveSpell(SpellType spellType, BukkitTask task){
        List<BukkitTask> tasks = new ArrayList<>();
        if(activeSpells.containsKey(spellType)){
            tasks = activeSpells.get(spellType);
        }
        tasks.add(task);
        activeSpells.put(spellType, tasks);
    }

    public void merge(PlayerData previousData){
        this.maxRegen = previousData.maxRegen;
        this.regenAmount = previousData.regenAmount;
        this.config = previousData.config;
    }

    public void prepareDataRemoval(){
        saveData();
    }

}
