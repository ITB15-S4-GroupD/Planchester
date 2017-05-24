package TeamF.Domain.enums.properties;

import TeamF.Domain.interfaces.DomainEntityProperty;

public enum EventDutyProperty implements DomainEntityProperty {
    ID,
    NAME,
    DESCRIPTION,
    START_DATE,
    END_DATE,
    EVENT_TYPE,
    EVENT_STATUS,
    CONDUCTOR,
    LOCATION,
    REHEARSAL_FOR,
    DEFAULT_POINTS,
    INSTRUMENTATION,
    DISPOSITION_LIST,
    MUSICAL_WORK_LIST,
    SECTION_DUTY_ROSTER_LIST,
    REQUEST_LIST,

    // dependencies
    START_TIME,
    END_TIME,
    ALTERNATIVE_INSTRUMENTATION_LIST
}
