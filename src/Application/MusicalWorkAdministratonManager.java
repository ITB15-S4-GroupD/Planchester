package Application;

import Application.DTO.EventDutyDTO;
import Domain.EventDutyModel;
import Persistence.Entities.EventDutyEntity;
import Persistence.Entities.MusicalWorkEntity;
import Persistence.EventDutyRDBMapper;
import Persistence.Mapper;
import Persistence.MusicalWorkRDBMapper;
import Persistence.PersistanceFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class MusicalWorkAdministratonManager {
    private static PersistanceFacade persistanceFacade = new PersistanceFacade();

    public static  List<String> getAllMusicalWorks() {
        List<String> musicalWorksList= new ArrayList<String>();

        List<MusicalWorkEntity> musicalWorks = MusicalWorkRDBMapper.getAllMusicalWorks();
        for(MusicalWorkEntity musicalWork : musicalWorks) {
            musicalWorksList.add(musicalWork.getName());
        }
        return musicalWorksList;
    }
}