package TeamF.jsonconnector.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;
import java.io.IOException;
import java.io.Writer;

public class WriteHelper {
    public static boolean writeJSONObject(Writer writer, JSONObjectEntity jsonObjectEntity) {
        if(writer != null && jsonObjectEntity != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            try {
                mapper.writeValue(writer, jsonObjectEntity);
                return true;
            } catch (IOException e) {
            }
        }

        return false;
    }

    public static String writeJSONObjectAsString(JSONObjectEntity jsonObjectEntity) {
        if(jsonObjectEntity != null) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                return mapper.writeValueAsString(jsonObjectEntity);
            } catch (IOException e) {
            }
        }

        return null;
    }
}
