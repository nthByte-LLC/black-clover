package net.dohaw.blackclover.config;

import net.dohaw.blackclover.grimmoire.Grimmoire;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.playerdata.*;
import net.dohaw.corelib.Config;

import java.io.File;
import java.util.*;

public class PlayerDataConfig extends Config {

//    private final List<GrimmoireType> hasSpecialPlayerData = Arrays.asList(
//        GrimmoireType.SHAKUDO, GrimmoireType.WATER,
//        GrimmoireType.SNOW, GrimmoireType.ASH, GrimmoireType.COTTON,
//        GrimmoireType.FUNGUS, GrimmoireType.COMPASS, GrimmoireType.GRAVITY, GrimmoireType.TRAP, GrimmoireType.TRANSFORMATION, GrimmoireType.DARK
//    );

    public PlayerDataConfig(File file){
        super(file);
    }

    public PlayerData loadData(UUID uuid){

        String grimmoireTypeStr = config.getString("Grimmoire Type");
        GrimmoireType grimmoireType = GrimmoireType.valueOf(grimmoireTypeStr.toUpperCase());
        GrimmoireWrapper grimmoireWrapper = (GrimmoireWrapper) Grimmoire.getByKey(grimmoireType);
        PlayerData pd;

        if(grimmoireType == GrimmoireType.SHAKUDO) {
            pd = new ShakudoPlayerData(uuid);
        }else if(grimmoireType == GrimmoireType.WATER) {
            pd = new WaterPlayerData(uuid);
        }else if(grimmoireType == GrimmoireType.SNOW) {
            pd = new SnowPlayerData(uuid);
        }else if(grimmoireType == GrimmoireType.ASH) {
            pd = new AshPlayerData(uuid);
        }else if(grimmoireType == GrimmoireType.COTTON) {
            pd = new CottonPlayerData(uuid);
        }else if(grimmoireType == GrimmoireType.FUNGUS) {
            pd = new FungusPlayerData(uuid);
        }else if(grimmoireType == GrimmoireType.COMPASS) {
            pd = new CompassPlayerData(uuid);
        }else if(grimmoireType == GrimmoireType.GRAVITY) {
            pd = new GravityPlayerData(uuid);
        }else if(grimmoireType == GrimmoireType.TRAP) {
            pd = new TrapPlayerData(uuid);
        }else if(grimmoireType == GrimmoireType.TRANSFORMATION) {
            pd = new TransformationPlayerData(uuid);
        }else if(grimmoireType == GrimmoireType.DARK){
            pd = new DarkPlayerData(uuid);
        }else{
            // If it gets to here, then there's a conflict between what's in the list and what is being checked in the if statement chain.
            pd = new PlayerData(uuid, grimmoireWrapper);
        }

        List<String> unlockedSpellsStr = config.getStringList("Unlocked Spells");
        EnumSet<SpellType> unlockedSpells = EnumSet.noneOf(SpellType.class);

        for(String unlockedSpell : unlockedSpellsStr){
            unlockedSpells.add(SpellType.valueOf(unlockedSpell));
        }

        pd.setMaxRegen(config.getInt("Max Regen"));
        pd.setRegenAmount(config.getInt("Regen Amount"));
        pd.setLevel(config.getInt("Level"));
        pd.setExperience(config.getDouble("Experience"));
        pd.setNumUnusedPoints(config.getInt("Unused Points"));
        pd.setUnlockedSpells(unlockedSpells);

        pd.setConfig(this);

        if(pd instanceof SpecifiableData){
            ((SpecifiableData) pd).loadSpecifiedData(config);
        }

        return pd;

    }

    public void saveData(PlayerData pd){

        EnumSet<SpellType> unlockedSpells = pd.getUnlockedSpells();
        List<String> unlockedSpellsStr = new ArrayList<>();

        for(SpellType spellType : unlockedSpells){
            unlockedSpellsStr.add(spellType.toString());
        }

        config.set("Max Regen", pd.getMaxRegen());
        config.set("Regen Amount", pd.getRegenAmount());
        config.set("Grimmoire Type", pd.getGrimmoireWrapper().getKEY().toString());
        config.set("Level", pd.getLevel());
        config.set("Experience", pd.getExperience());
        config.set("Unused Points", pd.getNumUnusedPoints());
        config.set("Unlocked Spells", unlockedSpellsStr);

        if(pd instanceof SpecifiableData){
            SpecifiableData spd = (SpecifiableData) pd;
            spd.saveSpecifiedData(config);
        }
        saveConfig();

    }

}
