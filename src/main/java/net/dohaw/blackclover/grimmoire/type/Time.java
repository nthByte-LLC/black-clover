package net.dohaw.blackclover.grimmoire.type;

import com.google.gson.internal.$Gson$Preconditions;
import javafx.scene.paint.Stop;
import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.time.*;

import java.util.Arrays;
import java.util.List;

public class Time extends GrimmoireWrapper {

    public StopTime stopTime;
    public Regeneration regeneration;
    public AlterTime alterTime;
    public TimeBeam timeBeam;
    public TimeSkip timeSkip;

    public Time() {
        super(GrimmoireType.TIME);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("time");
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

        this.timeSkip = new TimeSkip(config);
        this.spells.put(SpellType.TIME_SKIP, timeSkip);

        this.timeBeam = new TimeBeam(config);
        this.spells.put(SpellType.TIME_BEAM, timeSkip);

        this.alterTime = new AlterTime(config);
        this.spells.put(SpellType.ALTER_TIME, alterTime);

        this.regeneration = new Regeneration(config);
        this.spells.put(SpellType.REGENERATION, regeneration);

        this.stopTime = new StopTime(config);
        this.spells.put(SpellType.STOP_TIME, stopTime);

    }

}
