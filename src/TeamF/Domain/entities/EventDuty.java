package TeamF.Domain.entities;

import TeamF.Domain.enums.EventStatus;
import TeamF.Domain.enums.EventType;
import TeamF.Domain.interfaces.DomainEntity;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EventDuty implements DomainEntity {
    private int _eventDutyID;
    private String _name;
    private String _description;
    private LocalDateTime _startTime;
    private LocalDateTime _endTime;
    private EventType _eventType;
    private EventStatus _eventStatus;
    private String _conductor;
    private String _location;
    private EventDuty _rehearsalFor;
    private double _defaultPoints;
    private Instrumentation _maxInstrumentation;
    private List<MusicalWork> _musicalWorkList = new LinkedList<>();
    private List<Instrumentation> _instrumentationList = new LinkedList<>();

    public int getEventDutyID() {
        return _eventDutyID;
    }

    public void setEventDutyID(int eventDutyID) {
        _eventDutyID = eventDutyID;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getEndDate(String format) {
        if (_endTime != null) {
            Date date = Date.from(_endTime.atZone(ZoneId.systemDefault()).toInstant());

            Format formatter = new SimpleDateFormat(format);
            String result = formatter.format(date);

            return result;
        }

        return "";
    }

    public String getStartDate(String format) {
        if (_startTime != null) {
            Date date = Date.from(_startTime.atZone(ZoneId.systemDefault()).toInstant());

            Format formatter = new SimpleDateFormat(format);
            String result = formatter.format(date);

            return result;
        }

        return "";
    }

    public String getStartTime(String format) {
        if (_startTime != null) {
            Date date = Date.from(_startTime.atZone(ZoneId.systemDefault()).toInstant());

            Format formatter = new SimpleDateFormat(format);
            String result = formatter.format(date);

            return result;
        }

        return "";
    }

    public String getEndTime(String format) {
        if (_endTime != null) {
            Date date = Date.from(_endTime.atZone(ZoneId.systemDefault()).toInstant());

            Format formatter = new SimpleDateFormat(format);
            String result = formatter.format(date);

            return result;
        }

        return "";
    }

    public LocalDateTime getStartTime() {
        return _startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        _startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return _endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        _endTime = endTime;
    }

    public EventType getEventType() {
        return _eventType;
    }

    public void setEventType(EventType eventType) {
        _eventType = eventType;
    }

    public EventStatus getEventStatus() {
        return _eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        _eventStatus = eventStatus;
    }

    public String getConductor() {
        return _conductor;
    }

    public void setConductor(String conductor) {
        _conductor = conductor;
    }

    public String getLocation() {
        return _location;
    }

    public void setLocation(String location) {
        _location = location;
    }

    public EventDuty getRehearsalFor() {
        return _rehearsalFor;
    }

    public void setRehearsalFor(EventDuty rehearsalFor) {
        _rehearsalFor = rehearsalFor;
    }

    public double getDefaultPoints() {
        return _defaultPoints;
    }

    public void setDefaultPoints(double defaultPoints) {
        _defaultPoints = defaultPoints;
    }

    public Instrumentation getInstrumentation() {
        return _maxInstrumentation;
    }

    public void setInstrumentation(Instrumentation instrumentation) {
        _maxInstrumentation = instrumentation;
    }

    public void addMusicalWork(MusicalWork musicalWork, Instrumentation instrumentation) {
        _musicalWorkList.add(musicalWork);
        _instrumentationList.add(instrumentation);
    }

    public List<MusicalWork> getMusicalWorkList() {
        return _musicalWorkList;
    }

    public void setMusicalWorkList(List musicalWorkList) {
        this._musicalWorkList = musicalWorkList;
    }

    public List<Instrumentation> getInstrumentationList() {
        return _instrumentationList;
    }

    public void setInstrumentationList(List<Instrumentation> instrumentationList) {
        this._instrumentationList = instrumentationList;
    }

    public Instrumentation getMaxInstrumentation() {
        return _maxInstrumentation;
    }

    public void setMaxInstrumentation(Instrumentation maxInstrumentation) {
        _maxInstrumentation = maxInstrumentation;
    }

    @Override
    public int getID() {
        return getEventDutyID();
    }
}