package Persistence;

import Persistence.Entities.StringInstrumentationEntity;
import Persistence.Entities.WoodInstrumentationEntity;

/**
 * Created by julia on 10.04.2017.
 */
public class StringInstrumentationRDBMapper extends Mapper<StringInstrumentationEntity> {
    @Override
    Class<StringInstrumentationEntity> getEntityClass() {
        return StringInstrumentationEntity.class;
    }
}