package net.dohaw.blackclover.grimmoire.type;

import net.dohaw.blackclover.grimmoire.GrimmoireClassType;
import net.dohaw.blackclover.grimmoire.GrimmoireType;
import net.dohaw.blackclover.grimmoire.GrimmoireWrapper;
import net.dohaw.blackclover.grimmoire.spell.SpellType;
import net.dohaw.blackclover.grimmoire.spell.type.vortex.*;

import java.util.Arrays;
import java.util.List;

public class Vortex extends GrimmoireWrapper {

    public ElementStorm elementStorm;
    public Portnado portnado;
    public Earthstorm earthstorm;
    public Waterhose waterhose;
    public Firado firado;
    public Tornado tornado;

    public Vortex() {
        super(GrimmoireType.VORTEX);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("vortex");
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public GrimmoireClassType getClassType() {
        return GrimmoireClassType.DAMAGE;
    }

    @Override
    public void initSpells() {

        this.tornado = new Tornado(config);
        this.spells.put(SpellType.TORNADO, tornado);

        this.firado = new Firado(config);
        this.spells.put(SpellType.FIRADO, firado);

        this.waterhose = new Waterhose(config);
        this.spells.put(SpellType.WATERHOSE, waterhose);

        this.earthstorm = new Earthstorm(config);
        this.spells.put(SpellType.EARTHSTORM, earthstorm);

        this.portnado = new Portnado(config);
        this.spells.put(SpellType.PORTNADO, portnado);

        this.elementStorm = new ElementStorm(config);
        this.spells.put(SpellType.ELEMENT_STORM, elementStorm);

    }

}
