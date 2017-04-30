package Persistence;

import Persistence.Entities.BrassInstrumentationEntity;
import Persistence.Entities.PercussionInstrumentationEntity;

/**
 * Created by julia on 10.04.2017.
 */
public class PercussionInstrumentationRDBMapper extends Mapper<PercussionInstrumentationEntity> {
    @Override
    Class<PercussionInstrumentationEntity> getEntityClass() {
        return PercussionInstrumentationEntity.class;
    }
}