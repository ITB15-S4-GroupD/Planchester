package Utils;

import javax.xml.bind.ValidationException;
import java.security.DomainCombiner;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by julia on 19.04.2017.
 */
public class Validator {

    public static void validateString(String s, int maxLength) throws ValidationException {
        if(s != null && s.length() > maxLength) {
            throw new ValidationException("Eventstatus wrong");
        }
    }

    public static void validateMandatoryString(String s) throws ValidationException {
        if(s == null || s.isEmpty()) {
            throw new ValidationException("Eventstatus wrong");
        }
    }

    public static void validateMandatoryString(String s, int maxLength) throws ValidationException {
        if(s == null || s.isEmpty() || s.length() > maxLength) {
            throw new ValidationException("Eventstatus wrong");
        }
    }

    public static void validateInteger(Integer i, int minValue, int maxValue) throws ValidationException {
        if(i != null && (i < minValue || i > maxValue)) {
            throw new ValidationException("Eventstatus wrong");
        }
    }

    public static void validateMandatoryInteger(Integer i) throws ValidationException {
        if(i == null) {
            throw new ValidationException("Eventstatus wrong");
        }
    }

    public static void validateMandatoryInteger(Integer i, int minValue, int maxValue) throws ValidationException {
        if(i == null || i < minValue || i > maxValue) {
            throw new ValidationException("Eventstatus wrong");
        }
    }

    public static void validateDouble(Double d, double minValue, double maxValue) throws ValidationException {
        if(d != null && (d < minValue || d > maxValue)) {
            throw new ValidationException("Eventstatus wrong");
        }
    }

    public static void validateMandatoryDouble(Double d) throws ValidationException {
        if(d == null) {
            throw new ValidationException("Eventstatus wrong");
        }
    }

    public static void validateMandatoryDouble(Double d, double minValue, double maxValue) throws ValidationException {
        if(d == null || d < minValue || d > maxValue) {
            throw new ValidationException("Eventstatus wrong");
        }
    }

    public static void validateMandatoryTimestamp(Timestamp timestamp) throws ValidationException {
        if(timestamp == null) {
            throw new ValidationException("Eventstatus wrong");

        }
    }

    public static void validateTimestampAfterToday(Timestamp timestamp) throws ValidationException {
        if(timestamp != null && timestamp.before(new Date())) {
            throw new ValidationException("Eventstatus wrong");
        }
    }

    public static void validateTimestamp1BeforeTimestamp2(Timestamp timestamp1, Timestamp timestamp2) throws ValidationException {
        if(timestamp1 != null && timestamp2 != null && timestamp1.after(timestamp2)) {
            throw new ValidationException("Eventstatus wrong");
        }
    }
}