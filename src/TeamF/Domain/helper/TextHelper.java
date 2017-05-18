package TeamF.Domain.helper;

public class TextHelper {
    public static String getSeparatedText(char separator, int... values) {
        String result = null;

        if(values != null) {
            String[] tmpArray = new String[values.length];

            for(int i = 0; i < values.length; i++) {
                tmpArray[i] = String.valueOf(values[i]);
            }

            result = getTextFromString(separator, tmpArray);
        }

        return result;
    }

    public static String getTextFromString(char separator, String... values) {
        StringBuilder builder = new StringBuilder();

        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                builder.append(values[i]);

                if (i + 1 < values.length) {
                    builder.append(separator);
                }
            }
        }

        return builder.toString();
    }
}
