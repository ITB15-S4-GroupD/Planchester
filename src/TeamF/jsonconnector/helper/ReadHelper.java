package TeamF.jsonconnector.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;
import java.io.IOException;
import java.io.Reader;

public class ReadHelper {
    public static <T extends JSONObjectEntity> JSONObjectEntity readJSONObject(Reader reader, Class<T> jsonObjectEntityClass) {
        if(reader != null && jsonObjectEntityClass != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            mapper.registerModule(new JavaTimeModule());

            try {
                return mapper.readValue(reader, jsonObjectEntityClass);
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return null;
    }

    public static <T extends JSONObjectEntity> JSONObjectEntity readJSONObject(String content, Class<T> jsonObjectEntityClass) {
        if(content != null && jsonObjectEntityClass != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            mapper.registerModule(new JavaTimeModule());

            try {
                return mapper.readValue(content, jsonObjectEntityClass);
            } catch (IOException e) {
            }
        }

        return null;
    }
}
