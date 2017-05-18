package TeamF.Domain.helper;

public class IntegerHelper {
    public static boolean isValidId(int id) {
        if (id >= 0) {
            return true;
        }

        return false;
    }

    public static boolean isPositiveDefaultPoint(double points) {
        if (points >= 0) {
            return true;
        }

        return false;
    }

    public static boolean isBiggerThanZero (int count) {
        if (count > 0) {
            return true;
        }
        return false;
    }
}
