package TeamF.Domain.entities;

import TeamF.Domain.enums.InstrumentType;
import TeamF.Domain.interfaces.DomainEntity;

public class Instrument implements DomainEntity {
    private int _instrumentID;
    private InstrumentType _instrumentType;
    private String _brand;
    private String _model;

    public int getInstrumentID() {
        return _instrumentID;
    }

    public InstrumentType getInstrumentType() {
        return _instrumentType;
    }

    public String getBrand() {
        return _brand;
    }

    public String getModel() {
        return _model;
    }

    public void setInstrumentType(InstrumentType instrumentType) {
        _instrumentType = instrumentType;
    }

    public void setBrand(String brand) {
        _brand = brand;
    }

    public void setModel(String model) {
        _model = model;
    }

    public void setInstrumentID(int instrumentID) {
        _instrumentID = instrumentID;
    }

    @Override
    public int getID() {
        return getInstrumentID();
    }
}
