package net.dohaw.blackclover.playerdata;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.blackclover.config.PlayerDataConfig;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    @Getter
    private HashSet<SpellType> spellsOnCooldown = new HashSet<>();

    @Getter
    private Map<SpellType, BukkitTask> activeSpells = new HashMap<>();

    @Getter @Setter
    private int maxMana, manaAmount;

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

    public void saveData(){
        config.saveData(this);
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public boolean hasSufficientManaForSpell(CastSpellWrapper spell){
        return manaAmount >= spell.getRegenConsumed();
    }

    public boolean isSpellOnCooldown(SpellType spellType){
        return spellsOnCooldown.contains(spellType);
    }

    public boolean isSpellActive(SpellType spellType){
        return activeSpells.containsKey(spellType);
    }

    public void removeActiveSpell(SpellType spellType){
        BukkitTask runnable = activeSpells.get(spellType);
        runnable.cancel();
        activeSpells.remove(spellType);
    }

}
