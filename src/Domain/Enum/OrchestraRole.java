package Domain.Enum;

/**
 * Created by julia on 13.04.2017.
 */
public enum OrchestraRole {
    Concertmaster, Section_leader, Tuttiplayer, Soloist;

    public static boolean contains(String s) {
        for (OrchestraRole enumValue : values()) {
            if (enumValue.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
