package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.anti.*;
import net.dohaw.corelib.StringUtils;
import net.dohaw.corelib.helpers.ItemStackHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Anti extends GrimmoireWrapper {

    private ItemStack reflectionSword;
    private ItemStack antiMagicSword;

    public AntiSword antiSword;
    public Reflection reflection;
    public DemonForm demonForm;
    public Disable disable;
    public DemonJump demonJump;
    public DemonScratch demonScratch;

    public Anti() {
        super(GrimmoireType.ANTI);
        createAntiMagicSword();
        createReflectionSword();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("anti");
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.SPECIAL;
    }

    @Override
    public void initSpells() {

        this.disable = new Disable(config);
        this.spells.put(SpellType.DISABLE, disable);

        this.demonJump = new DemonJump(config);
        this.spells.put(SpellType.DEMON_JUMP, demonJump);

        this.demonScratch = new DemonScratch(config);
        this.spells.put(SpellType.DEMON_SCRATCH, demonScratch);

        this.reflection = new Reflection(config);
        this.spells.put(SpellType.REFLECTION, reflection);

        this.demonForm = new DemonForm(config);
        this.spells.put(SpellType.DEMON_FORM, demonForm);

        this.antiSword = new AntiSword(config);
        this.spells.put(SpellType.ANTI_SWORD, antiSword);

    }

    private void createReflectionSword(){
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(StringUtils.colorString("&8Reflection Sword"));
        sword.setItemMeta(meta);
        ItemStackHelper.addGlowToItem(sword);
        this.reflectionSword = sword;
    }

    private void createAntiMagicSword(){
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(StringUtils.colorString("&8Anti Magic Sword"));
        sword.setItemMeta(meta);
        ItemStackHelper.addGlowToItem(sword);
        this.antiMagicSword = sword;
    }

    public ItemStack getReflectionSword(){
        return reflectionSword.clone();
    }

    public ItemStack getAntiSword(){
        return antiMagicSword.clone();
    }



}
