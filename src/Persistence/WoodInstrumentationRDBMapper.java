package Persistence;

import Persistence.Entities.EventDutyEntity;
import Persistence.Entities.WoodInstrumentationEntity;
import Utils.DateHelper;
import org.hibernate.Session;

import java.util.Calendar;
import java.util.List;

/**
 * Created by julia on 10.04.2017.
 */
public class WoodInstrumentationRDBMapper extends Mapper<WoodInstrumentationEntity> {
    @Override
    Class<WoodInstrumentationEntity> getEntityClass() {
        return WoodInstrumentationEntity.class;
    }
}