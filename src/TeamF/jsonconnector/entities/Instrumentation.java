package TeamF.jsonconnector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.helper.TextHelper;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Instrumentation implements JSONObjectEntity {
    private int _instrumentationID;

    //WoodInstrumentation
    private int _flute;
    private int _oboe;
    private int _clarinet;
    private int _bassoon;

    //StringInstrumentation
    private int _violin1;
    private int _violin2;
    private int _viola;
    private int _violincello;
    private int _doublebass;

    //BrassInstrumentation
    private int _horn;
    private int _trumpet;
    private int _trombone;
    private int _tube;

    //PercussionInstrumentation
    private int _kettledrum;
    private int _percussion;
    private int _harp;

    private List<SpecialInstrumentation> _special;

    @JsonGetter("id")
    public int getInstrumentationID() {
        return _instrumentationID;
    }

    @JsonGetter("flute")
    public int getFlute() {
        return _flute;
    }

    @JsonGetter("oboe")
    public int getOboe() {
        return _oboe;
    }

    @JsonGetter("clarinet")
    public int getClarinet() {
        return _clarinet;
    }

    @JsonGetter("bassoon")
    public int getBassoon() {
        return _bassoon;
    }

    @JsonGetter("violin1")
    public int getViolin1() {
        return _violin1;
    }

    @JsonGetter("violin2")
    public int getViolin2() {
        return _violin2;
    }

    @JsonGetter("viola")
    public int getViola() {
        return _viola;
    }

    @JsonGetter("violincello")
    public int getViolincello() {
        return _violincello;
    }

    @JsonGetter("doublebass")
    public int getDoublebass() {
        return _doublebass;
    }

    @JsonGetter("horn")
    public int getHorn() {
        return _horn;
    }

    @JsonGetter("trumpet")
    public int getTrumpet() {
        return _trumpet;
    }

    @JsonGetter("trombone")
    public int getTrombone() {
        return _trombone;
    }

    @JsonGetter("tube")
    public int getTube() {
        return _tube;
    }

    @JsonGetter("kettledrum")
    public int getKettledrum() {
        return _kettledrum;
    }

    @JsonGetter("percussion")
    public int getPercussion() {
        return _percussion;
    }

    @JsonGetter("harp")
    public int getHarp() {
        return _harp;
    }

    @JsonGetter("special_instrumentation")
    public List<SpecialInstrumentation> getSpecialInstrumentation() {
        return _special;
    }

    @JsonSetter("id")
    public void setInstrumentationID(int instrumentationID) {
        _instrumentationID = instrumentationID;
    }

    @JsonSetter("flute")
    public void setFlute(int flute) {
        _flute = flute;
    }

    @JsonSetter("oboe")
    public void setOboe(int oboe) {
        _oboe = oboe;
    }

    @JsonSetter("clarinet")
    public void setClarinet(int clarinet) {
        _clarinet = clarinet;
    }

    @JsonSetter("bassoon")
    public void setBassoon(int bassoon) {
        _bassoon = bassoon;
    }

    @JsonSetter("violin1")
    public void setViolin1(int violin1) {
        _violin1 = violin1;
    }

    @JsonSetter("violin2")
    public void setViolin2(int violin2) {
        _violin2 = violin2;
    }

    @JsonSetter("viola")
    public void setViola(int viola) {
        _viola = viola;
    }

    @JsonSetter("violincello")
    public void setViolincello(int violincello) {
        _violincello = violincello;
    }

    @JsonSetter("doublebass")
    public void setDoublebass(int doublebass) {
        _doublebass = doublebass;
    }

    @JsonSetter("horn")
    public void setHorn(int horn) {
        _horn = horn;
    }

    @JsonSetter("trumpet")
    public void setTrumpet(int trumpet) {
        _trumpet = trumpet;
    }

    @JsonSetter("trombone")
    public void setTrombone(int trombone) {
        _trombone = trombone;
    }

    @JsonSetter("tube")
    public void setTube(int tube) {
        _tube = tube;
    }

    @JsonSetter("kettledrum")
    public void setKettledrum(int kettledrum) {
        _kettledrum = kettledrum;
    }

    @JsonSetter("percussion")
    public void setPercussion(int percussion) {
        _percussion = percussion;
    }

    @JsonSetter("harp")
    public void setHarp(int harp) {
        _harp = harp;
    }

    @JsonSetter("special_instrumentation")
    public void setSpecialInstrumentation(List<SpecialInstrumentation> _special) {
        this._special = _special;
    }


    @Override
    public String getEntityName() {
        return "Instrumentation";
    }

    @Override
    public String getDisplayName() {
        // @TODO: construct the string correctly
        return getEntityName();
    }

    public String getWoodInstrumentationText() {
        return "Wood: " + TextHelper.getSeparatedText('/', getFlute(), getOboe(), getClarinet(), getBassoon());
    }

    public String getStringInstrumentationText() {
        return "String: " + TextHelper.getSeparatedText('/', getViolin1(), getViolin2(), getViola(), getViolincello(), getDoublebass());
    }

    public String getBrassInstrumentiaton() {
        return "Brass: " + TextHelper.getSeparatedText('/', getHorn(), getTrumpet(), getTrombone(), getTube());
    }

    public String getPercussionInstrumentation() {
        return "Percussion: " + TextHelper.getSeparatedText('/', getKettledrum(), getPercussion(), getHarp());
    }

    @Override
    public String toString() {
        StringBuilder specialInstrumentation = new StringBuilder();

        if(_special != null) {
            if(_special.size() > 0) {
                specialInstrumentation.append("\n");
            }

            for(int i = 0; i < _special.size(); i++) {
                specialInstrumentation.append(_special.get(i).getDisplayName());

                if(i < _special.size() -1) {
                    specialInstrumentation.append(" + ");
                }
            }
        }

        return getStringInstrumentationText() + "\n" +
               getWoodInstrumentationText() + "\n" +
               getBrassInstrumentiaton() + "\n" +
               getPercussionInstrumentation() +
               specialInstrumentation.toString();

    }

    public String getInstrumentation(){
        return toString();
    }

}
