package Persistence;

import Persistence.Entities.BrassInstrumentationEntity;
import Persistence.Entities.WoodInstrumentationEntity;

/**
 * Created by julia on 10.04.2017.
 */
public class BrassInstrumentationRDBMapper extends Mapper<BrassInstrumentationEntity> {
    @Override
    Class<BrassInstrumentationEntity> getEntityClass() {
        return BrassInstrumentationEntity.class;
    }
}