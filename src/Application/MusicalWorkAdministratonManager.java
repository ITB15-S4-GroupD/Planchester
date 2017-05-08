package Application;

import Application.DTO.InstrumentationDTO;
import Application.DTO.MusicalWorkDTO;
import Persistence.Entities.InstrumentationEntity;
import Persistence.Entities.MusicalWorkEntity;
import Persistence.PersistanceFacade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class MusicalWorkAdministratonManager {
    private static PersistanceFacade persistanceFacade = new PersistanceFacade(MusicalWorkEntity.class);

    public static List<MusicalWorkDTO> getAllMusicalWorks() {
        List<MusicalWorkDTO> musicalWorksList= new ArrayList<>();

        List<MusicalWorkEntity> musicalWorks = persistanceFacade.list(null);
        for(MusicalWorkEntity musicalWork : musicalWorks) {
            musicalWorksList.add(getMusicalWorkDTO(musicalWork));
        }
        return musicalWorksList;
    }

    private static MusicalWorkDTO getMusicalWorkDTO(MusicalWorkEntity musicalWorkEntity) {
        MusicalWorkDTO musicalWorkDTO = new MusicalWorkDTO();
        musicalWorkDTO.setId(musicalWorkEntity.getMusicalWorkId());
        musicalWorkDTO.setName(musicalWorkEntity.getName());
        musicalWorkDTO.setComposer(musicalWorkEntity.getComposer());
        musicalWorkDTO.setInstrumentation(getInstrumentationDTO(musicalWorkEntity.getInstrumentationByInstrumentationId()));
        musicalWorkDTO.setName(musicalWorkEntity.getName());
        return musicalWorkDTO;
    }

    private static InstrumentationDTO getInstrumentationDTO(InstrumentationEntity instrumentationEntity) {
        InstrumentationDTO instrumentationDTO = new InstrumentationDTO();
        instrumentationDTO.setBasson(instrumentationEntity.getWoodInstrumentationByWoodInstrumentation().getBassoon());
        instrumentationDTO.setClarinet(instrumentationEntity.getWoodInstrumentationByWoodInstrumentation().getClarinet());
        instrumentationDTO.setDescription(instrumentationEntity.getPercussionInstrumentationByPercussionInstrumentation().getPercussionDescription());
        instrumentationDTO.setDoublebass(instrumentationEntity.getStringInstrumentationByStringInstrumentation().getDoublebass());
        instrumentationDTO.setFirstViolin(instrumentationEntity.getStringInstrumentationByStringInstrumentation().getViolin1());
        instrumentationDTO.setFlute(instrumentationEntity.getWoodInstrumentationByWoodInstrumentation().getFlute());
        instrumentationDTO.setHarp(instrumentationEntity.getPercussionInstrumentationByPercussionInstrumentation().getHarp());
        instrumentationDTO.setHorn(instrumentationEntity.getBrassInstrumentationByBrassInstrumentation().getHorn());
        instrumentationDTO.setKettledrum(instrumentationEntity.getPercussionInstrumentationByPercussionInstrumentation().getKettledrum());
        instrumentationDTO.setOboe(instrumentationEntity.getWoodInstrumentationByWoodInstrumentation().getOboe());
        instrumentationDTO.setPercussion(instrumentationEntity.getPercussionInstrumentationByPercussionInstrumentation().getPercussion());
        instrumentationDTO.setSecondViolin(instrumentationEntity.getStringInstrumentationByStringInstrumentation().getViolin2());
        instrumentationDTO.setTrombone(instrumentationEntity.getBrassInstrumentationByBrassInstrumentation().getTrombone());
        instrumentationDTO.setTrumpet(instrumentationEntity.getBrassInstrumentationByBrassInstrumentation().getTrumpet());
        instrumentationDTO.setTube(instrumentationEntity.getBrassInstrumentationByBrassInstrumentation().getTube());
        instrumentationDTO.setViola(instrumentationEntity.getStringInstrumentationByStringInstrumentation().getViola());
        instrumentationDTO.setVioloncello(instrumentationEntity.getStringInstrumentationByStringInstrumentation().getViolincello());
        return instrumentationDTO;
    }
}