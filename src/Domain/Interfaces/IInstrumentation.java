package Domain.Interfaces;

import com.sun.org.glassfish.gmbal.Description;

/**
 * Created by julia on 08.05.2017.
 */
public interface IInstrumentation {
    int getFirstViolin();
    void setFirstViolin(int firstViolin);
    int getSecondViolin();
    void setSecondViolin(int secondViolin);
    int getViola();
    void setViola(int viola);
    int getVioloncello();
    void setVioloncello(int violoncello);
    int getDoublebass();
    void setDoublebass(int doublebass);
    int getHorn();
    void setHorn(int horn);
    int getTrumpet();
    void setTrumpet(int trumpet);
    int getTrombone();
    void setTrombone(int trombone);
    int getTube();
    void setTube(int tube);
    int getFlute();
    void setFlute(int flute);
    int getOboe();
    void setOboe(int oboe);
    int getClarinet();
    void setClarinet(int clarinet);
    int getBasson();
    void setBasson(int basson);
    int getKettledrum();
    void setKettledrum(int kettledrum);
    int getPercussion();
    void setPercussion(int percussion);
    int getHarp();
    void setHarp(int harp);
    String getDescription();
    void setDescription(String description);
}
