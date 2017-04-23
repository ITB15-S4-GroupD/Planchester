package Domain.Enum;

/**
 * Created by julia on 13.04.2017.
 */
public enum SectionType {
    Violin1, Violin2, Viola, Violincello, Doublebass, Woodwind, Brass, Percussion;

    public static boolean contains(String s) {
        for (SectionType enumValue : values()) {
            if (enumValue.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
