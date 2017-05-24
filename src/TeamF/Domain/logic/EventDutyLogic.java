package TeamF.Domain.logic;

import javafx.util.Pair;
import TeamF.Domain.entities.EventDuty;
import TeamF.Domain.entities.Instrumentation;
import TeamF.Domain.entities.MusicalWork;
import TeamF.Domain.enums.properties.EventDutyProperty;
import TeamF.Domain.enums.EventStatus;
import TeamF.Domain.enums.EventType;
import TeamF.Domain.helper.DateTimeHelper;
import TeamF.Domain.helper.IntegerHelper;
import TeamF.Domain.helper.StringHelper;
import TeamF.Domain.interfaces.EntityLogic;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import static TeamF.Domain.enums.properties.EventDutyProperty.*;

public class EventDutyLogic implements EntityLogic<EventDuty, EventDutyProperty> {
    protected EventDutyLogic() {
    }

    @Override
    public List<Pair<String, String>> validate(EventDuty eventDuty, EventDutyProperty... eventDutyproperty) {
        List<Pair<String, String>> resultList = new LinkedList<>();

        LOOP:
        for (EventDutyProperty property : eventDutyproperty) {

            switch (property) {
                case ID:
                    if (!IntegerHelper.isValidId(eventDuty.getEventDutyID())) {
                        resultList.add(new Pair<>(String.valueOf(ID), "is not in the correct range"));
                    }

                    break;

                case EVENT_TYPE:
                    if (eventDuty.getEventType() == null) {
                        resultList.add(new Pair<>(String.valueOf(EVENT_TYPE), "is empty"));
                    } else {
                        boolean isValid = false;
                        for (EventType eventType : EventType.values()) {
                            if (String.valueOf(eventType).equals(String.valueOf(eventDuty.getEventType()))) {
                                isValid = true;
                            }
                        }

                        if (!isValid) {
                            resultList.add(new Pair<>(String.valueOf(EVENT_TYPE), "is not valid"));
                        }
                    }

                    break LOOP;

                case START_DATE:
                    if (eventDuty.getStartTime() != null) {
                        if (!DateTimeHelper.takesPlaceInFuture(eventDuty.getStartTime())) {
                            resultList.add(new Pair<>(String.valueOf(START_DATE), "is bygone"));
                        }
                        if (!DateTimeHelper.periodExpired(eventDuty.getStartTime())) {
                            resultList.add(new Pair<>(String.valueOf(START_DATE), "must take place at least 2 months in future "));
                        }
                    } else {
                        resultList.add(new Pair<>(String.valueOf(START_DATE), "is empty"));
                    }

                    break;

                case END_DATE:
                    if (eventDuty.getEndTime() != null) {
                        if (!DateTimeHelper.takesPlaceInFuture(eventDuty.getEndTime())) {
                            resultList.add(new Pair<>(String.valueOf(END_DATE), "is bygone"));
                        }
                        if (!DateTimeHelper.compareDates(eventDuty.getStartTime(), eventDuty.getEndTime())) {
                            resultList.add(new Pair<>(String.valueOf(END_DATE), "is before Start Time"));
                        }
                        if (eventDuty.getEventType().toString().equals(EventType.Concert.toString()) || eventDuty.getEventType().toString().equals(EventType.Hofkapelle.toString())
                                || eventDuty.getEventType().toString().equals(EventType.NonMusicalEvent.toString()) || eventDuty.getEventType().toString().equals(EventType.Rehearsal.toString())
                                || eventDuty.getEventType().toString().equals(EventType.Opera.toString())) {

                            if (!eventDuty.getEndDate("dd/MM/yyyy").equals(eventDuty.getStartDate("dd/MM/yyyy"))) {
                                resultList.add(new Pair<>(String.valueOf(END_DATE), "this type of event cannot be longer than one day"));
                            }
                        }
                    } else {
                        //TODO: if empty then default value 3h
                        resultList.add(new Pair<>(String.valueOf(END_DATE), "is empty"));
                    }

                    break;

                case DEFAULT_POINTS:
                    if (!IntegerHelper.isPositiveDefaultPoint(eventDuty.getDefaultPoints())) {
                        resultList.add(new Pair<>(String.valueOf(DEFAULT_POINTS), "Only positive Points possible"));
                    }

                    break;

                case NAME:
                    if (eventDuty.getName() == null && !StringHelper.isNotEmpty(eventDuty.getName())) {
                        resultList.add(new Pair<>(String.valueOf(NAME), "is empty"));
                    }
                    break;

                case LOCATION:
                    if (eventDuty.getLocation() == null && !StringHelper.isNotEmpty(eventDuty.getLocation())) {
                        resultList.add(new Pair<>(String.valueOf(LOCATION), "is empty"));
                    }

                    break;

                case CONDUCTOR:
                    if (eventDuty.getConductor() == null && !StringHelper.isNotEmpty(eventDuty.getConductor())) {
                        resultList.add(new Pair<>(String.valueOf(CONDUCTOR), "is empty"));
                    }

                    break;

                case EVENT_STATUS:
                    if (eventDuty.getEventStatus() == null) {
                        resultList.add(new Pair<>(String.valueOf(EVENT_STATUS), "is empty"));
                    } else {
                        boolean isValid = false;
                        for (EventStatus eventStatus : EventStatus.values()) {
                            if (String.valueOf(eventStatus).equals(eventDuty.getEventStatus())) {
                                isValid = true;
                            }
                        }

                        if (!isValid) {
                            resultList.add(new Pair<>(String.valueOf(EVENT_STATUS), "is not valid"));
                        }
                    }

                    break;

                case REHEARSAL_FOR:
                    if (eventDuty.getRehearsalFor() == null || IntegerHelper.isValidId(eventDuty.getRehearsalFor().getEventDutyID())) {
                        resultList.add(new Pair<>(String.valueOf(REHEARSAL_FOR), "is not valid"));
                    } else {
                        if (eventDuty.getRehearsalFor().getRehearsalFor() != null) {
                            resultList.add(new Pair<>(String.valueOf(REHEARSAL_FOR), "can not be assigned to type Rehearsal"));

                        }
                        if (DateTimeHelper.compareRehearsalDate(eventDuty.getRehearsalFor().getStartTime(), eventDuty.getEndTime())) {
                            resultList.add(new Pair<>(String.valueOf(REHEARSAL_FOR), "can not be after Event"));
                        }
                    }

                    break;
                case MUSICAL_WORK_LIST:

                    Set<Pair<MusicalWork, Instrumentation>> set = new HashSet<>(eventDuty.getMusicalWorkList().size());
                    List<Instrumentation> iList = new LinkedList<>();
                    List<MusicalWork> wList = new LinkedList<>();

                    for (int i = 0; i < eventDuty.getMusicalWorkList().size() && i < eventDuty.getInstrumentationList().size(); i++) {
                        Pair<MusicalWork, Instrumentation> pair = new Pair<>(eventDuty.getMusicalWorkList().get(i), eventDuty.getInstrumentationList().get(i));
                        set.add(pair);
                    }

                    for (Pair pair : set) {
                        wList.add((MusicalWork) pair.getKey());
                        iList.add((Instrumentation) pair.getValue());
                    }

                    eventDuty.setMusicalWorkList(wList);
                    eventDuty.setInstrumentationList(iList);

                    break;
            }
        }

        return resultList;
    }

    @Override
    public List<Pair<String, String>> validate(EventDuty eventDuty) {
        return validate(eventDuty, EventDutyProperty.values());
    }
}
