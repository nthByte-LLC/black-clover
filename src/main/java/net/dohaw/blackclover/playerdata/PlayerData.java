package net.dohaw.blackclover.playerdata;

import net.dohaw.blackclover.config.PlayerDataConfig;
import net.dohaw.blackclover.exception.UnexpectedPlayerData;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.ActivatableSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.CastSpellWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.SpellWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class PlayerData {

    private int level;

    private double currentXP;

    private boolean willTakeFallDamage = true;

    /*
        Whether the player is floating with the Float spell in the Gravity grimmoire.
     */
    private boolean isFloating;

    // The y level at which they started floating
    private double floatY;

    /*
        For spells that have a cast time like "Freeze" from the Snow grimmoire
     */
    private boolean isCurrentlyCasting;

    private double castStartHealth;

    private SpellType spellCurrentlyCasting;

    private boolean canCast = true;

    private boolean isFrozen;

    private int frozenStacks = 0;

    private boolean isInVulnerable;

    private boolean canAttack;

    private EnumSet<SpellType> spellsOnCooldown = EnumSet.noneOf(SpellType.class);

    private EnumMap<SpellType, List<BukkitTask>> spellRunnables = new EnumMap<>(SpellType.class);

    private int maxRegen, regenAmount;

    private GrimmoireWrapper grimmoireWrapper;

    private PlayerDataConfig config;

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

    public UUID getUUID(){
        return uuid;
    }

    public boolean hasSufficientRegenForSpell(CastSpellWrapper spell){
        return regenAmount >= spell.getRegenConsumed();
    }

    public boolean isSpellOnCooldown(SpellType spellType){
        return spellsOnCooldown.contains(spellType);
    }

    /**
     * If the spell has runnables running for it (Particle stuff for the most part). Usually, spell runnables will cancel themselves when they're done. If they're not done, they won't be canceled
     */
    public boolean isSpellActive(SpellType spellType){
        if(spellRunnables.containsKey(spellType)){
            List<BukkitTask> runnables = spellRunnables.get(spellType);
            for(BukkitTask task : runnables){
                if(!task.isCancelled()){
                    return true;
                }
            }
        }
        return false;
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

    public void addSpellRunnables(SpellType spellType, BukkitTask ...task){
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
        // Removes all effects of activatable spells
        for(SpellWrapper wrapper : grimmoireWrapper.getSpells().values()){
            if(wrapper instanceof ActivatableSpellWrapper){
                try {
                    ((ActivatableSpellWrapper)wrapper).deactiveSpell(this);
                } catch (UnexpectedPlayerData unexpectedPlayerData) {
                    unexpectedPlayerData.printStackTrace();
                }
            }
        }
        getPlayer().setGlowing(false);
        stopAllRunnables();
        saveData();
    }

    public GrimmoireType getGrimmoireType(){
        return grimmoireWrapper.getKEY();
    }

    public void setFrozen(JavaPlugin plugin, int duration){

        if(isFrozen){
            frozenStacks++;
        }else{
            this.isFrozen = true;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            this.isFrozen = false;
            if(frozenStacks != 0){
                frozenStacks--;
                setFrozen(plugin, duration);
            }
        }, duration * 20);

    }

    public boolean isWillTakeFallDamage() {
        return willTakeFallDamage;
    }

    public void setWillTakeFallDamage(boolean willTakeFallDamage) {
        this.willTakeFallDamage = willTakeFallDamage;
    }

    public boolean isFloating() {
        return isFloating;
    }

    public void setFloating(boolean floating) {
        isFloating = floating;
    }

    public double getFloatY() {
        return floatY;
    }

    public void setFloatY(double floatY) {
        this.floatY = floatY;
    }

    public boolean isCurrentlyCasting() {
        return isCurrentlyCasting;
    }

    public void setCurrentlyCasting(boolean currentlyCasting) {
        isCurrentlyCasting = currentlyCasting;
    }

    public double getCastStartHealth() {
        return castStartHealth;
    }

    public void setCastStartHealth(double castStartHealth) {
        this.castStartHealth = castStartHealth;
    }

    public SpellType getSpellCurrentlyCasting() {
        return spellCurrentlyCasting;
    }

    public void setSpellCurrentlyCasting(SpellType spellCurrentlyCasting) {
        this.spellCurrentlyCasting = spellCurrentlyCasting;
    }

    public void setCanCast(boolean canCast) {
        this.canCast = canCast;
    }

    public boolean isCanCast() {
        return canCast;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public boolean isInVulnerable() {
        return isInVulnerable;
    }

    public void setInVulnerable(boolean inVulnerable) {
        isInVulnerable = inVulnerable;
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public EnumSet<SpellType> getSpellsOnCooldown() {
        return spellsOnCooldown;
    }

    public Map<SpellType, List<BukkitTask>> getSpellRunnables() {
        return spellRunnables;
    }

    public int getMaxRegen() {
        return maxRegen;
    }

    public void setMaxRegen(int maxRegen) {
        this.maxRegen = maxRegen;
    }

    public int getRegenAmount() {
        return regenAmount;
    }

    public void setRegenAmount(int regenAmount) {
        this.regenAmount = regenAmount;
    }

    public GrimmoireWrapper getGrimmoireWrapper() {
        return grimmoireWrapper;
    }

    public void setGrimmoireWrapper(GrimmoireWrapper grimmoireWrapper) {
        this.grimmoireWrapper = grimmoireWrapper;
    }

    public PlayerDataConfig getConfig() {
        return config;
    }

    public void setConfig(PlayerDataConfig config) {
        this.config = config;
    }

}
